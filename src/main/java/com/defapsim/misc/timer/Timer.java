package com.defapsim.misc.timer;

/**
 * The timer is used to determine the execution time of a simulation.
 */

public class Timer {

    private long millisStart;
    private long millisEnd;
    private long nanosStart;
    private long nanosEnd;

    public Timer() {
        //EMPTY STANDARD CONSTRUCTOR
    }

    /**
     * Start the timer
     */
    public void start() {
        this.millisStart = System.currentTimeMillis();
        this.nanosStart = System.nanoTime();
    }

    /**
     * End the timer
     */
    public void stop() {
        this.millisEnd = System.currentTimeMillis();
        this.nanosEnd = System.nanoTime();
    }

    /**
     * Get the time duration between the start and the end of the timer
     * @return      A string representing the time duration between the start and the end of the timer
     */
    public String getTimeMS() {
        long milliTime = this.millisEnd - this.millisStart;
        int[] out = new int[] {0, 0, 0, 0};

        out[0] = (int)(milliTime / 3600000      );
        out[1] = (int)(milliTime / 60000        ) % 60;
        out[2] = (int)(milliTime / 1000         ) % 60;
        out[3] = (int)(milliTime)                 % 1000;

        return formatTimeArray(out);
    }

    /**
     * Format the time duration between the start and the end of the timer into a string
     */
    private String formatTimeArray(int[] timeArray) {
        return timeArray[0] + "h : " + timeArray[1] + "m : " + timeArray[2] + "s : " + timeArray[3] + "ms";
    }

    /**
     * Get the time duration between the start and the end of the timer in milliseconds
     * @return      A Float representing the time duration between the start and the end of the timer in milliseconds
     */
    public Float getTimeInMilliseconds() {
        return (float) ((this.millisEnd - this.millisStart));
    }

    /**
     * Get the time duration between the start and the end of the timer in nanoseconds
     * @return      A Float representing the time duration between the start and the end of the timer in nanoseconds
     */
    public Float getTimeInNanoseconds() {
        return (this.nanosEnd - this.nanosStart) / 1000000.F;
    }
}
