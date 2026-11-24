use macroquad::audio::{load_sound, play_sound, PlaySoundParams, Sound};
use macroquad::prelude::*;
use roxmltree::Document;
use std::collections::HashMap;

const WORLD_WIDTH: f32 = 600.0;
const MIN_WORLD_HEIGHT: f32 = 800.0;
const MAX_WORLD_HEIGHT: f32 = 1400.0;
const PLAYER_SIZE: f32 = 50.0;
const PLAYER_RADIUS: f32 = PLAYER_SIZE / 2.0;
const OBSTACLE_SIZE: f32 = 60.0;
const OBSTACLE_RADIUS: f32 = OBSTACLE_SIZE / 2.0;

fn window_conf() -> Conf {
    Conf {
        window_title: "Flappy Circle".to_owned(),
        window_width: 600,
        window_height: 800,
        high_dpi: true,
        ..Default::default()
    }
}

#[derive(Clone, Copy, PartialEq, Eq)]
enum Screen {
    Menu,
    Settings,
    Languages,
    About,
    Rankings,
    Gameplay,
}

#[derive(Clone, Copy)]
struct Viewport {
    scale: f32,
    offset_x: f32,
    offset_y: f32,
    width: f32,
    height: f32,
}

impl Viewport {
    fn new() -> Self {
        let height = (screen_height() / screen_width() * WORLD_WIDTH)
            .clamp(MIN_WORLD_HEIGHT, MAX_WORLD_HEIGHT);
        let scale = (screen_width() / WORLD_WIDTH).min(screen_height() / height);
        Self {
            scale,
            offset_x: (screen_width() - WORLD_WIDTH * scale) / 2.0,
            offset_y: (screen_height() - height * scale) / 2.0,
            width: WORLD_WIDTH,
            height,
        }
    }

    fn rect(self, x: f32, y: f32, width: f32, height: f32) -> (f32, f32, f32, f32) {
        (
            self.offset_x + x * self.scale,
            screen_height() - self.offset_y - (y + height) * self.scale,
            width * self.scale,
            height * self.scale,
        )
    }

    fn draw_rect(self, x: f32, y: f32, width: f32, height: f32, color: Color) {
        let (x, y, width, height) = self.rect(x, y, width, height);
        draw_rectangle(x, y, width, height, color);
    }

    fn draw_texture(
        self,
        texture: &Texture2D,
        x: f32,
        y: f32,
        width: f32,
        height: f32,
        color: Color,
    ) {
        let (x, y, width, height) = self.rect(x, y, width, height);
        draw_texture_ex(
            texture,
            x,
            y,
            color,
            DrawTextureParams {
                dest_size: Some(vec2(width, height)),
                ..Default::default()
            },
        );
    }

    fn draw_glyph(
        self,
        texture: &Texture2D,
        glyph: Glyph,
        x: f32,
        bottom: f32,
        scale: f32,
    ) {
        let (x, y, width, height) = self.rect(x, bottom, glyph.width * scale, glyph.height * scale);
        draw_texture_ex(
            texture,
            x,
            y,
            WHITE,
            DrawTextureParams {
                dest_size: Some(vec2(width, height)),
                source: Some(Rect::new(
                    glyph.x,
                    glyph.y,
                    glyph.width,
                    glyph.height,
                )),
                ..Default::default()
            },
        );
    }
}

#[derive(Clone, Copy, Debug)]
struct Glyph {
    x: f32,
    y: f32,
    width: f32,
    height: f32,
    x_offset: f32,
    y_offset: f32,
    x_advance: f32,
}

struct BitmapFont {
    texture: Texture2D,
    glyphs: HashMap<char, Glyph>,
    line_height: f32,
    base: f32,
}

impl BitmapFont {
    async fn load() -> Self {
        let texture = load_texture("assets/font/font.png")
            .await
            .expect("Could not load assets/font/font.png");
        texture.set_filter(FilterMode::Linear);

        let descriptor = include_str!("../assets/font/font.fnt");
        let mut glyphs = HashMap::new();
        let mut line_height = 39.0;
        let mut base = 29.0;

        for line in descriptor.lines() {
            if line.starts_with("common ") {
                let values = attributes(line);
                line_height = number(&values, "lineHeight");
                base = number(&values, "base");
            } else if line.starts_with("char ") {
                let values = attributes(line);
                let Some(character) = values
                    .get("id")
                    .and_then(|id| id.parse::<u32>().ok())
                    .and_then(char::from_u32)
                else {
                    continue;
                };
                glyphs.insert(
                    character,
                    Glyph {
                        x: number(&values, "x"),
                        y: number(&values, "y"),
                        width: number(&values, "width"),
                        height: number(&values, "height"),
                        x_offset: number(&values, "xoffset"),
                        y_offset: number(&values, "yoffset"),
                        x_advance: number(&values, "xadvance"),
                    },
                );
            }
        }

        assert!(!glyphs.is_empty(), "The bitmap font has no glyphs");
        Self {
            texture,
            glyphs,
            line_height,
            base,
        }
    }

    fn glyph(&self, character: char) -> Glyph {
        self.glyphs
            .get(&character)
            .or_else(|| self.glyphs.get(&'?'))
            .copied()
            .unwrap_or(Glyph {
                x: 0.0,
                y: 0.0,
                width: 0.0,
                height: 0.0,
                x_offset: 0.0,
                y_offset: 0.0,
                x_advance: 16.0,
            })
    }

    fn text_width(&self, text: &str) -> f32 {
        text.chars().map(|character| self.glyph(character).x_advance).sum()
    }

    fn draw_line(&self, viewport: Viewport, text: &str, x: f32, baseline: f32) {
        let mut cursor = x;
        for character in text.chars() {
            let glyph = self.glyph(character);
            if glyph.width > 0.0 && glyph.height > 0.0 {
                let bottom =
                    baseline + self.base - glyph.y_offset - glyph.height * 1.75;
                viewport.draw_glyph(
                    &self.texture,
                    glyph,
                    cursor + glyph.x_offset,
                    bottom,
                    1.0,
                );
            }
            cursor += glyph.x_advance;
        }
    }

    fn draw_centered(&self, viewport: Viewport, text: &str, center_x: f32, baseline: f32) {
        self.draw_line(
            viewport,
            text,
            center_x - self.text_width(text) / 2.0,
            baseline,
        );
    }

    fn draw_wrapped(
        &self,
        viewport: Viewport,
        text: &str,
        center_x: f32,
        top: f32,
        max_width: f32,
    ) {
        let lines = self.wrap(text, max_width);
        for (index, line) in lines.iter().enumerate() {
            self.draw_centered(
                viewport,
                line,
                center_x,
                top - 32.0 - index as f32 * self.line_height,
            );
        }
    }

    fn wrap(&self, text: &str, max_width: f32) -> Vec<String> {
        let mut lines = Vec::new();
        for paragraph in text.split('\n') {
            let mut line = String::new();
            for word in paragraph.split_whitespace() {
                let candidate = if line.is_empty() {
                    word.to_owned()
                } else {
                    format!("{line} {word}")
                };
                if !line.is_empty() && self.text_width(&candidate) > max_width {
                    lines.push(std::mem::take(&mut line));
                }
                if !line.is_empty() {
                    line.push(' ');
                }
                line.push_str(word);
            }
            lines.push(line);
        }
        lines
    }
}

fn attributes(line: &str) -> HashMap<&str, &str> {
    line.split_whitespace()
        .skip(1)
        .filter_map(|field| field.split_once('='))
        .collect()
}

fn number(values: &HashMap<&str, &str>, key: &str) -> f32 {
    values
        .get(key)
        .and_then(|value| value.parse::<f32>().ok())
        .unwrap_or(0.0)
}

struct Language {
    code: String,
    name: String,
    strings: HashMap<String, String>,
}

fn load_languages() -> Vec<Language> {
    let document = Document::parse(include_str!("../assets/languages.xml"))
        .expect("Could not parse assets/languages.xml");
    document
        .descendants()
        .filter(|node| node.has_tag_name("language"))
        .map(|node| {
            let code = node.attribute("name").unwrap_or_default().to_owned();
            let name = node.attribute("full_name").unwrap_or(&code).to_owned();
            let strings = node
                .children()
                .filter(|child| child.has_tag_name("string"))
                .filter_map(|child| {
                    Some((
                        child.attribute("key")?.to_owned(),
                        child.attribute("value")?.to_owned(),
                    ))
                })
                .collect();
            Language {
                code,
                name,
                strings,
            }
        })
        .collect()
}

struct Saves {
    language: String,
    high_score: u32,
    last_score: u32,
}

impl Saves {
    async fn load() -> Self {
        let contents = match load_file("save.dat").await {
            Ok(bytes) => String::from_utf8(bytes)
                .unwrap_or_else(|error| panic!("Save file is not valid UTF-8: {error}")),
            Err(error) => {
                eprintln!("No saved game data loaded ({error}); starting with defaults.");
                String::new()
            }
        };
        let values: HashMap<_, _> = contents
            .lines()
            .filter_map(|line| line.split_once('='))
            .collect();
        Self {
            language: values.get("language").copied().unwrap_or("en").to_owned(),
            high_score: values
                .get("high_score")
                .and_then(|value| value.parse().ok())
                .unwrap_or(0),
            last_score: values
                .get("last_score")
                .and_then(|value| value.parse().ok())
                .unwrap_or(0),
        }
    }

    async fn store(&self) {
        let contents = format!(
            "language={}\nhigh_score={}\nlast_score={}\n",
            self.language, self.high_score, self.last_score
        );
        std::fs::write("save.dat", contents.as_bytes())
            .unwrap_or_else(|error| panic!("Could not save game data: {error}"));
    }
}

struct Assets {
    circle: Texture2D,
    pixel: Texture2D,
    click: Sound,
    hit: Sound,
    explosion: Sound,
    font: BitmapFont,
}

impl Assets {
    async fn load() -> Self {
        let circle = load_texture("assets/textures/circle.png")
            .await
            .expect("Could not load assets/textures/circle.png");
        let pixel = load_texture("assets/textures/pixel.png")
            .await
            .expect("Could not load assets/textures/pixel.png");
        let click = load_sound("assets/sounds/click.wav")
            .await
            .expect("Could not load assets/sounds/click.wav");
        let hit = load_sound("assets/sounds/hit.wav")
            .await
            .expect("Could not load assets/sounds/hit.wav");
        let explosion = load_sound("assets/sounds/explosion.ogg")
            .await
            .expect("Could not load assets/sounds/explosion.ogg");
        let font = BitmapFont::load().await;
        Self {
            circle,
            pixel,
            click,
            hit,
            explosion,
            font,
        }
    }

    fn click(&self) {
        play_sound(
            &self.click,
            PlaySoundParams {
                looped: false,
                volume: 1.0,
            },
        );
    }
}

struct Player {
    x: f32,
    y: f32,
    vx: f32,
    vy: f32,
    world_width: f32,
    world_height: f32,
    alive: bool,
}

impl Player {
    fn new() -> Self {
        Self {
            x: WORLD_WIDTH / 2.0,
            y: -PLAYER_RADIUS,
            vx: 0.0,
            vy: 900.0,
            world_width: WORLD_WIDTH,
            world_height: MIN_WORLD_HEIGHT,
            alive: true,
        }
    }

    fn reset(&mut self, world_height: f32) {
        self.x = self.world_width / 2.0;
        self.y = -PLAYER_RADIUS;
        self.vx = 0.0;
        self.vy = 900.0;
        self.world_height = world_height;
        self.alive = true;
    }

    fn jump(&mut self, direction: f32) {
        if self.y + PLAYER_RADIUS <= self.world_height {
            self.vy = 400.0;
            self.vx = direction * 500.0;
        }
    }

    fn kill(&mut self, assets: &Assets) {
        if self.alive {
            self.alive = false;
            play_sound(
                &assets.explosion,
                PlaySoundParams {
                    looped: false,
                    volume: 1.0,
                },
            );
        }
    }

    fn update(&mut self, delta: f32, assets: &Assets) {
        self.x += self.vx * delta;
        self.y += self.vy * delta;
        self.vy -= 900.0 * delta;
        self.vx = if self.vx > 0.0 {
            (self.vx - 750.0 * delta).max(0.0)
        } else {
            (self.vx + 750.0 * delta).min(0.0)
        };

        if self.x < PLAYER_RADIUS {
            self.x = PLAYER_RADIUS;
            self.vx = self.vx.abs();
            play_sound(
                &assets.hit,
                PlaySoundParams {
                    looped: false,
                    volume: 1.0,
                },
            );
        }
        if self.x + PLAYER_RADIUS > self.world_width {
            self.x = self.world_width - PLAYER_RADIUS;
            self.vx = -self.vx.abs();
            play_sound(
                &assets.hit,
                PlaySoundParams {
                    looped: false,
                    volume: 1.0,
                },
            );
        }
        if self.y <= PLAYER_RADIUS && self.vy <= 0.0 {
            self.kill(assets);
        }
    }

    fn draw(&self, viewport: Viewport, assets: &Assets) {
        viewport.draw_texture(
            &assets.circle,
            self.x - PLAYER_RADIUS,
            self.y - PLAYER_RADIUS,
            PLAYER_SIZE,
            PLAYER_SIZE,
            WHITE,
        );
    }
}

#[derive(Clone, Copy)]
struct Obstacle {
    x: f32,
    y: f32,
}

struct Game {
    player: Player,
    obstacles: Vec<Obstacle>,
    score: f32,
    running: bool,
    pause_time: f32,
    paused: bool,
    safe_count: i32,
    lowest: usize,
    world_height: f32,
}

impl Game {
    fn new(world_height: f32) -> Self {
        let mut game = Self {
            player: Player::new(),
            obstacles: (0..15).map(|_| Obstacle { x: 0.0, y: 0.0 }).collect(),
            score: 0.0,
            running: true,
            pause_time: 0.0,
            paused: false,
            safe_count: 4,
            lowest: 0,
            world_height,
        };
        game.reset(world_height);
        game
    }

    fn reset(&mut self, world_height: f32) {
        self.world_height = world_height;
        self.player.reset(world_height);
        self.score = 0.0;
        self.running = true;
        self.pause_time = 0.0;
        self.paused = false;
        self.safe_count = 4;
        self.obstacles[0].x = macroquad::rand::gen_range(0.0, WORLD_WIDTH);
        self.obstacles[0].y = -OBSTACLE_SIZE - PLAYER_SIZE;
        self.lowest = 0;
        for index in 1..self.obstacles.len() {
            self.wrap_obstacle(index);
        }
    }

    fn resize(&mut self, world_height: f32) {
        self.world_height = world_height;
        self.player.world_height = world_height;
    }

    fn wrap_obstacle(&mut self, index: usize) {
        let lowest = self.obstacles[self.lowest];
        let obstacle = &mut self.obstacles[index];
        obstacle.y = lowest.y - 100.0;
        if self.safe_count <= 0 {
            self.safe_count = 4;
            obstacle.x = if lowest.x < WORLD_WIDTH / 2.0 {
                lowest.x + 250.0
            } else {
                lowest.x - 250.0
            };
        } else {
            obstacle.x = macroquad::rand::gen_range(0.0, WORLD_WIDTH);
            if (obstacle.x - lowest.x).abs() < 250.0 {
                self.safe_count -= 1;
            } else {
                self.safe_count = 4;
            }
        }
        self.lowest = index;
    }

    fn update(&mut self, delta: f32, assets: &Assets) -> bool {
        if self.paused {
            return false;
        }
        if self.pause_time > 0.0 {
            self.pause_time -= delta * 2.0;
            return false;
        }
        if is_key_pressed(KeyCode::Left) || is_key_pressed(KeyCode::A) {
            self.player.jump(-1.0);
        }
        if is_key_pressed(KeyCode::Right) || is_key_pressed(KeyCode::D) {
            self.player.jump(1.0);
        }

        self.player.update(delta, assets);
        for obstacle in &mut self.obstacles {
            obstacle.y += delta * 120.0;
        }

        for index in 0..self.obstacles.len() {
            if self.obstacles[index].y > self.world_height + OBSTACLE_SIZE {
                self.wrap_obstacle(index);
            }
            let dx = self.player.x - self.obstacles[index].x;
            let dy = self.player.y - self.obstacles[index].y;
            let min_distance = PLAYER_RADIUS + OBSTACLE_RADIUS;
            if dx * dx + dy * dy < min_distance * min_distance {
                self.player.kill(assets);
            }
        }

        if self.player.alive {
            self.score += delta * 10.0;
        } else {
            self.running = false;
        }
        self.running
    }

    fn draw(&self, viewport: Viewport, assets: &Assets) {
        self.player.draw(viewport, assets);
        for obstacle in &self.obstacles {
            viewport.draw_texture(
                &assets.circle,
                obstacle.x - OBSTACLE_RADIUS,
                obstacle.y - OBSTACLE_RADIUS,
                OBSTACLE_SIZE,
                OBSTACLE_SIZE,
                Color::new(1.0, 0.5, 0.5, 1.0),
            );
        }
        assets.font.draw_centered(
            viewport,
            &((self.score as u32).to_string()),
            WORLD_WIDTH / 2.0,
            viewport.height - 20.0,
        );
        if self.pause_time > 0.0 && !self.paused {
            assets.font.draw_centered(
                viewport,
                &self.pause_time.ceil().to_string(),
                WORLD_WIDTH / 2.0,
                viewport.height / 2.0,
            );
        }
    }

    fn touch(&mut self, x: f32) {
        self.player.jump(if x > WORLD_WIDTH / 2.0 { 1.0 } else { -1.0 });
    }
}

struct App {
    assets: Assets,
    languages: Vec<Language>,
    language_index: usize,
    saves: Saves,
    screen: Screen,
    game: Option<Game>,
    fade: f32,
    last_game_over_saved: bool,
}

impl App {
    async fn load() -> Self {
        let assets = Assets::load().await;
        let languages = load_languages();
        assert!(!languages.is_empty(), "No languages found in assets/languages.xml");
        let saves = Saves::load().await;
        let language_index = languages
            .iter()
            .position(|language| language.code == saves.language)
            .or_else(|| languages.iter().position(|language| language.code == "en"))
            .unwrap_or(0);
        Self {
            assets,
            languages,
            language_index,
            saves,
            screen: Screen::Menu,
            game: None,
            fade: 1.0,
            last_game_over_saved: false,
        }
    }

    fn text<'a>(&'a self, key: &'a str) -> &'a str {
        self.languages[self.language_index]
            .strings
            .get(key)
            .map(String::as_str)
            .unwrap_or(key)
    }

    fn buttons(&self) -> Vec<String> {
        match self.screen {
            Screen::Menu => ["exit", "settings", "rankings", "play"]
                .iter()
                .map(|key| self.text(key).to_owned())
                .collect(),
            Screen::Settings => ["back", "language", "about"]
                .iter()
                .map(|key| self.text(key).to_owned())
                .collect(),
            Screen::Languages => {
                let mut buttons = vec![self.text("back").to_owned()];
                buttons.extend(
                    self.languages
                        .iter()
                        .map(|language| language.name.clone()),
                );
                buttons
            }
            Screen::About => vec![self.text("back").to_owned(), "GitHub".to_owned()],
            Screen::Rankings => vec![self.text("back").to_owned()],
            Screen::Gameplay => Vec::new(),
        }
    }

    fn enter(&mut self, screen: Screen) {
        if screen == Screen::Gameplay {
            self.game = Some(Game::new(Viewport::new().height));
            self.last_game_over_saved = false;
        }
        self.screen = screen;
        self.fade = 1.0;
    }

    async fn button(&mut self, index: usize) -> bool {
        self.assets.click();
        match self.screen {
            Screen::Menu => match index {
                0 => return false,
                1 => self.enter(Screen::Settings),
                2 => self.enter(Screen::Rankings),
                3 => self.enter(Screen::Gameplay),
                _ => {}
            },
            Screen::Settings => match index {
                0 => self.enter(Screen::Menu),
                1 => self.enter(Screen::Languages),
                2 => self.enter(Screen::About),
                _ => {}
            },
            Screen::Languages => {
                if index == 0 {
                    self.enter(Screen::Settings);
                } else if let Some((language_index, _)) = self.languages.iter().enumerate().nth(index - 1) {
                    self.language_index = language_index;
                    self.saves.language = self.languages[language_index].code.clone();
                    self.saves.store().await;
                    self.enter(Screen::Settings);
                }
            }
            Screen::About if index == 0 => self.enter(Screen::Settings),
            Screen::About if index == 1 => {
                webbrowser::open("https://github.com/oostap2/Flappy")
                    .unwrap_or_else(|error| eprintln!("Could not open project page: {error}"));
            }
            Screen::Rankings if index == 0 => self.enter(Screen::Menu),
            _ => {}
        }
        true
    }

    async fn update_input(&mut self, viewport: Viewport) -> bool {
        if self.fade > 0.0 {
            return true;
        }
        if self.screen == Screen::Gameplay {
            if is_key_pressed(KeyCode::Escape) {
                if let Some(game) = self.game.as_mut() {
                    if game.paused {
                        game.paused = false;
                    } else {
                        game.pause_time = 3.0;
                        game.paused = true;
                    }
                }
            }
            if is_mouse_button_pressed(MouseButton::Left) {
                let (mouse_x, _) = mouse_position();
                let world_x = (mouse_x - viewport.offset_x) / viewport.scale;
                if let Some(game) = self.game.as_mut() {
                    game.touch(world_x);
                }
            }
            return true;
        }

        let buttons = self.buttons();
        let mut pressed_index = None;
        if is_mouse_button_pressed(MouseButton::Left) {
            let (mouse_x, mouse_y) = mouse_position();
            let x = (mouse_x - viewport.offset_x) / viewport.scale;
            let y = viewport.height - (mouse_y - viewport.offset_y) / viewport.scale;
            for index in 0..buttons.len() {
                let button_y = 80.0 + 20.0 + (60.0 + 20.0) * index as f32;
                if x > 20.0 && x < WORLD_WIDTH - 20.0 && y > button_y && y < button_y + 60.0 {
                    pressed_index = Some(index);
                    break;
                }
            }
        }

        if pressed_index.is_none() {
            pressed_index = (0..buttons.len().min(8)).find(|index| {
                let key = match index {
                    0 => KeyCode::Key1,
                    1 => KeyCode::Key2,
                    2 => KeyCode::Key3,
                    3 => KeyCode::Key4,
                    4 => KeyCode::Key5,
                    5 => KeyCode::Key6,
                    6 => KeyCode::Key7,
                    _ => KeyCode::Key8,
                };
                is_key_pressed(key)
            });
        }
        if let Some(index) = pressed_index {
            return self.button(index).await;
        }
        true
    }

    fn draw(&self, viewport: Viewport) {
        viewport.draw_texture(
            &self.assets.pixel,
            0.0,
            0.0,
            viewport.width,
            viewport.height,
            SKYBLUE,
        );
        match self.screen {
            Screen::Gameplay => {
                if let Some(game) = &self.game {
                    game.draw(viewport, &self.assets);
                }
            }
            Screen::Menu | Screen::Settings | Screen::Languages | Screen::About | Screen::Rankings => {
                self.draw_static_screen(viewport);
            }
        }

        if self.fade > 0.0 {
            let (x, y, width, height) = viewport.rect(0.0, 0.0, viewport.width, viewport.height);
            draw_rectangle(
                x,
                y,
                width,
                height,
                Color::new(0.0, 0.0, 0.0, self.fade.clamp(0.0, 1.0)),
            );
        }
    }

    fn draw_static_screen(&self, viewport: Viewport) {
        if self.screen != Screen::Menu {
            let label = match self.screen {
                Screen::Settings => Some("settings"),
                Screen::Languages => Some("languages"),
                Screen::About => None,
                Screen::Rankings => Some("rankings"),
                _ => None,
            };
            if let Some(key) = label {
                self.assets.font.draw_wrapped(
                    viewport,
                    self.text(key),
                    WORLD_WIDTH / 2.0,
                    viewport.height - 10.0,
                    WORLD_WIDTH - 30.0,
                );
            }
        }

        if self.screen == Screen::About {
            self.assets.font.draw_wrapped(
                viewport,
                self.text("about_text"),
                WORLD_WIDTH / 2.0,
                viewport.height * 0.66,
                WORLD_WIDTH - 60.0,
            );
        } else if self.screen == Screen::Rankings {
            let content = if self.saves.high_score == 0 && self.saves.last_score == 0 {
                self.text("empty").to_owned()
            } else {
                format!(
                    "{}: {}\n{}: {}",
                    self.text("high_score"),
                    self.saves.high_score,
                    self.text("last_score"),
                    self.saves.last_score
                )
            };
            let lines = self.assets.font.wrap(&content, WORLD_WIDTH - 40.0);
            for (index, line) in lines.iter().enumerate() {
                self.assets.font.draw_centered(
                    viewport,
                    line,
                    WORLD_WIDTH / 2.0,
                    viewport.height - 110.0 - index as f32 * self.assets.font.line_height,
                );
            }
        }

        for (index, label) in self.buttons().iter().enumerate() {
            let y = 80.0 + 20.0 + (60.0 + 20.0) * index as f32;
            viewport.draw_rect(20.0, y, WORLD_WIDTH - 40.0, 60.0, BLACK);
            viewport.draw_rect(
                22.0,
                y + 2.0,
                WORLD_WIDTH - 44.0,
                56.0,
                Color::new(0.5, 0.5, 0.5, 1.0),
            );
            self.assets
                .font
                .draw_line(viewport, label, 20.0 + 20.0, y + 40.0);
        }
    }

    async fn update(&mut self, delta: f32, viewport: Viewport) -> bool {
        self.fade = (self.fade - delta * 4.0).max(0.0);
        if !self.update_input(viewport).await {
            return false;
        }
        if self.screen == Screen::Gameplay {
            if let Some(game) = self.game.as_mut() {
                game.resize(viewport.height);
                let still_running = game.update(delta, &self.assets);
                if !still_running && !game.paused && game.player.alive == false {
                    if !self.last_game_over_saved {
                        let score = game.score as u32;
                        self.saves.last_score = score;
                        self.saves.high_score = self.saves.high_score.max(score);
                        self.last_game_over_saved = true;
                        self.saves.store().await;
                    }
                    self.enter(Screen::Menu);
                }
            }
        }
        true
    }
}

#[macroquad::main(window_conf)]
async fn main() {
    let mut app = App::load().await;
    loop {
        let viewport = Viewport::new();
        clear_background(BLACK);
        app.draw(viewport);
        let delta = get_frame_time().min(0.1);
        if !app.update(delta, viewport).await {
            break;
        }
        next_frame().await;
    }
}
