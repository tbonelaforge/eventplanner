package eventplanner;

import eventplanner.EventPlanner;

import java.util.function.Function;

public class EventPlannerThread extends Thread {
    private int eventIndex;
    private EventPlanner eventPlanner;
    private long[] eventDurations; // for Testing.
    private Runnable event = new DefaultEvent();
    private Function<Integer, Void> callback;

    
    public EventPlannerThread(
                              EventPlanner eventPlanner,
                              long[] eventDurations,
                              Function<Integer, Void> callback
                              ) {
        super();
        this.eventPlanner = eventPlanner;
        this.eventDurations = eventDurations;
        this.callback = callback;
    }

    public EventPlannerThread(
                              EventPlanner eventPlanner,
                              Function<Integer, Void> callback
                              ) {
        super();
        this.eventPlanner = eventPlanner;
        this.callback = callback;
    }

    public EventPlannerThread(
                              EventPlanner eventPlanner,
                              Runnable event,
                              Function<Integer, Void> callback
                              ) {
        super();
        this.eventPlanner = eventPlanner;
        this.event = event;
        this.callback = callback;
    }
    
    @Override
    public void run() {
        System.out.printf("Inside thread %s, about to start, and the current time is; %f%n", getName(), eventPlanner.getCurrentTime());
        try {
            //int howManyTotalEvents = eventPlanner.howManyEventsRemaining();
            while (eventPlanner.howManyEventsRemaining() > 0
                   && eventPlanner.getCurrentTime() < eventPlanner.getFinalTime()) {
                long sleepTime = eventPlanner.getSleepTime();
                if (sleepTime > 0) {
                    //System.out.printf("Inside thread %s, About to sleep for %d%n", getName(), sleepTime);
                    Thread.sleep(sleepTime);
                } else {
                    System.out.printf("Inside thread %s, The sleep time was zero or negative: %d! Falling Behindi!!!%n", getName(), sleepTime);
                }
                //System.out.printf("Insidie thread %s, about to perform event, the current time is: %f%n", getName(), eventPlanner.getCurrentTime());
                performEvent();
            }
            System.out.printf("Inside thread %s, %d of the events were executed! (current time is: %f%n", getName(), eventPlanner.howManyEventsExecuted(), eventPlanner.getCurrentTime());
            if (callback != null) {
                callback.apply(eventPlanner.howManyEventsExecuted());
            }
        } catch(InterruptedException e) {
            System.out.printf("THREAD %s HAD AN EXCEPTION!!!! KILLING THREAD!!!!%n", getName());
            System.out.println(e);
        }
    }

    private void performEvent() throws InterruptedException {
        if (event != null) {
            //System.out.printf("Inside thread %s, about to start event%n", getName());
            eventPlanner.startEvent();
            event.run();
            eventPlanner.stopEvent();
            eventIndex += 1;
        }
    }

    private class DefaultEvent implements Runnable {
        @Override
        public void run() {
            long eventDuration = getEventDurationForTesting();
            System.out.printf("INside thread %s, about to start an event that will have duration: %d%n", getName(), eventDuration);
            try {
                Thread.sleep(eventDuration);
            } catch (InterruptedException e) {
                System.out.println("WTF! got an interrupted exception while trying ot sleep!");
                System.out.println(e);
            }
            System.out.printf("Inside thread %s, Finished event.%n", getName());
        }
    }
    
    private long getEventDurationForTesting() {
        if (eventDurations != null) {
            return eventDurations[eventIndex];
        }
        long hi = 1500;
        long lo = 750;
        double randDuration = (hi - lo * 1.0) * Math.random() + lo * 1.0;
        return (long) Math.ceil(randDuration);
    }
    
}
