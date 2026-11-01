package meow.cats.misc;

public class Zoom {
    public static Zoom INSTANCE;

    public boolean active = false;

    private final float[] fovLevels = { 40.0F, 20.0F, 10.0F, 4.0F };
    private int currentLevelIndex = 0;

    public Zoom() {
        INSTANCE = this;
    }
    public float getCurrentFov() {
        return fovLevels[currentLevelIndex];
    }

    public void zoomIn() {
        if (currentLevelIndex < fovLevels.length - 1) {
            currentLevelIndex++;
        }
    }

    public void zoomOut() {
        if (currentLevelIndex > 0) {
            currentLevelIndex--;
        }
    }

    public void reset() {
        currentLevelIndex = 0;
    }
}
