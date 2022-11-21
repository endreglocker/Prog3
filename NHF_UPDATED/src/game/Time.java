package game;

public class Time {

    private float millisPerCycle;

    private long lastUpdate;

    //private int elapsedCycles;

    private float excessCycles;

    private boolean isPaused;

    public Time(float cyclesPerSecond) {
        frameRate(cyclesPerSecond);
        reset();
    }

    public void frameRate(float cyclesPerSecond) {
        millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
    }

    public void reset() {
        //elapsedCycles = 0;
        excessCycles = 0.0f;
        lastUpdate = getCurrentTime();
        isPaused = false;
    }

    public void update() {
        //Get the current time and calculate the delta time.
        long currUpdate = getCurrentTime();
        float delta = (float) (currUpdate - lastUpdate) + excessCycles;

        //Update the number of elapsed and excess ticks if we're not paused.
        if (!isPaused) {
            //elapsedCycles += (int) Math.floor(delta / millisPerCycle);
            excessCycles = delta % millisPerCycle;
        }

        //Set the last update time for the next update cycle.
        lastUpdate = currUpdate;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    /*
    public boolean hasElapsedCycle() {
        if (elapsedCycles > 0) {
            elapsedCycles--;
            return true;
        }
        return false;
    }
    */
    private static long getCurrentTime() {
        return (System.nanoTime() / 1000000L);
    }

}
