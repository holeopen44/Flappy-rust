package oostap2.game.flappy;

public class MyMath {
    private static float requiredDistance;

    public static void InitMyMath(float playerRadius, float obstacleRadius) {
        requiredDistance = playerRadius + obstacleRadius;
    }

    public static boolean checkCircleCollision(float x1, float y1, float x2, float y2) {
        double currentDistance = Math.sqrt(sq(x1 - x2) + sq(y1 - y2));

        return currentDistance < requiredDistance;
    }

    public static boolean checkSquareCollision(float x1, float y1, float x2, float y2) {
        float currentXDistance = Math.abs(x1 - x2);
        boolean xCollision = currentXDistance < requiredDistance;

        float currentYDistance = Math.abs(y1 - y2);
        boolean yCollision = currentYDistance < requiredDistance;

        return xCollision && yCollision;
    }

    private static float sq(float n) {
        return n * n;
    }
}
