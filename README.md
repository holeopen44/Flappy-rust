# Flappy Circle

**One circle. A stream of obstacles. How long can you stay alive?**

Flappy Circle is a quick-to-learn arcade game about timing your jumps and dodging
oncoming circles. This Rust edition is built with [Macroquad](https://macroquad.rs/)
and brings the original game's art, sounds, bitmap font, and translations along
for the ride.

## Play

```sh
cargo run
```

### Controls

| Action | Controls |
| --- | --- |
| Jump left | `A`, `←`, or click the left half of the game |
| Jump right | `D`, `→`, or click the right half of the game |
| Pause or resume | `Esc` |
| Choose a menu option | Click it or press `1`–`8` |

Build an optimized version with:

```sh
cargo build --release
```

## Features

- Fast, momentum-based jumps and bouncing off the screen edges
- Increasing your score by staying in the game
- High-score and last-score tracking
- English, Ukrainian, and Polish menus
- Resizable play area that adapts to different window shapes

Your language choice and scores are stored in `save.dat` in the game's current
working directory.

## Development

Install [Rust](https://www.rust-lang.org/tools/install) with Cargo, then run
`cargo run` from the project directory. Check that the project compiles with:

```sh
cargo check
```

The game is a Rust and Macroquad rework of the original libGDX project. The
original game is also available on [itch.io](https://ostapkondur.itch.io/flappy-circle).
