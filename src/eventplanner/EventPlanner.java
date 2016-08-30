package eventplanner;

public class EventPlanner {
    private double l0; // Initial time for current event.
    private double l1; // Final time for current event.
    private int N; // Total number of events to plan.
    private int k; // Number of events executed;
    private double s; // Total time spent in events.
    private double t; // Current estimate for how long an event will take.
    private double f; // Final time at which all events should be finished.
    private double r; // Amount of time remaining for events to happen.
    private double d; // Total duration of event stream.

    public EventPlanner(int N, double d) {
        this.N = N;
        this.d = d;
    }
    
    public long getSleepTime() {
        f = getFinalTime();
        if (k == 0) {
            return 0L;
        }
        r  = f - getCurrentTime();
        int n = howManyEventsRemaining();
        double sleepTime = r / n - t;
        long longSleepTime = (long) Math.floor(sleepTime * 1000);
        return longSleepTime;
    }

    public void startEvent() {
        l0 = getCurrentTime();
    }

    public void stopEvent() {
        l1 = getCurrentTime();
        double l = l1 - l0;
        s = s + l;
        k = k + 1;
        t = s / k;
    }

    public int howManyEventsRemaining() {
        return N - k;
    }

    public int howManyEventsExecuted() {
        return k;
    }
    
    public double getFinalTime() {
        if (f <= 0.0) {
            f = getCurrentTime() + d;
        }
        return f;
    }
    
    public static double getCurrentTime() {
        long currentMillis = System.currentTimeMillis();
        return currentMillis / 1000.0;
    }
}
