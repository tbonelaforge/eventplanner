package eventplanner;

import eventplanner.EventPlanner;
import eventplanner.EventPlannerThread;
import eventplanner.GetSystemStatusEvent;

import java.util.function.Function;

public class EventPlannerTester {

    private static long[] eventDurations = {
        1000L,
        1250L,
        1500L,
        1120L,
        2000L,
        0750L,
        1000L,
        1250L
    };


    private int eventIndex;
    
    public static void main(String[] args) throws InterruptedException {
        EventPlannerTester eventPlannerTester = new EventPlannerTester();
        EventPlanner eventPlanner = new EventPlanner(8, 10.0);

        eventPlannerTester.testThreads();
    }

    public void test(EventPlanner eventPlanner) throws InterruptedException {
        //System.out.printf("About to test the event planner, the current time is: %f%n", eventPlanner.getCurrentTime());
        while (eventPlanner.howManyEventsRemaining() > 0) {
            long sleepTime = eventPlanner.getSleepTime();
            if (sleepTime > 0) {
                System.out.printf("About to sleep for %d%n", sleepTime);
                Thread.sleep(sleepTime);
            } else {
                System.out.printf("The sleep time was zero or negative: %d! Falling behind!!!%n", sleepTime);
            }
            System.out.printf("About to performEvent, the current time is: %f%n", eventPlanner.getCurrentTime());
            performEvent(eventPlanner);
        }
        System.out.println("All the events were executed!");
    }

    public void testThreads() throws InterruptedException {
        GetSystemStatusEvent getSystemStatusEvent = new GetSystemStatusEvent();
        int threadIndex = 0;
        double d = 10.0;
        int BigN = 800;
        //int BigN = 87;
        //        int T = 2; // This is tied to the eventDurations array.
        int T = 220;
        //int T = 45;
        double delta = d / BigN;
        double threadDuration = d - (T - 1) * delta;
        int howManyEventsDelegated = 0;
        ResultsCollector resultsCollector = new ResultsCollector(EventPlanner.getCurrentTime(), T);
        while (threadIndex < T) {
            int remainingEvents = BigN - howManyEventsDelegated;
            int howManyThreadsLeft = T - threadIndex;
            int littleN = (int) Math.ceil(remainingEvents * 1.0 / howManyThreadsLeft);
            System.out.printf("STARTING A NEW THREAD, with littleN = %d and threadDuration = %f%n", littleN, threadDuration);
            EventPlanner eventPlanner = new EventPlanner(littleN, threadDuration);
            /*
            EventPlannerThread eventPlannerThread = new EventPlannerThread(
                eventPlanner,
                eventDurations[threadIndex],
                resultsCollector
            );
            */
            /*
            EventPlannerThread eventPlannerThread = new EventPlannerThread(
                eventPlanner,
                resultsCollector
            );
            */
            EventPlannerThread eventPlannerThread = new EventPlannerThread(
                eventPlanner,
                getSystemStatusEvent,
                resultsCollector
            );
            eventPlannerThread.start();
            threadIndex = threadIndex + 1;
            howManyEventsDelegated = howManyEventsDelegated + littleN;
            Thread.sleep((long) Math.floor(delta * 1000));
        }
    }

    private void performEvent(EventPlanner eventPlanner) throws InterruptedException {
        long eventDuration = eventDurations[eventIndex];
        System.out.printf("Inside performEvent, about to start an event that will have duration: %d%n", eventDuration);
        eventPlanner.startEvent();
        Thread.sleep(eventDuration);
        System.out.println("Finished event");
        eventPlanner.stopEvent();
        eventIndex += 1;
    }

    private class ResultsCollector implements Function<Integer, Void> {
        private int howManyResultsExpected;
        private int howManyResultsCollected;
        private double startTime;
        private int total;

        public ResultsCollector(double startTime, int howManyResultsExpected) {
            this.startTime = startTime;
            this.howManyResultsExpected = howManyResultsExpected;
        }
        
        @Override
        public Void apply(Integer howMany) {
            howManyResultsCollected = howManyResultsCollected + 1;
            total = total + howMany;
            if (howManyResultsCollected >= howManyResultsExpected) {
                double endTime = System.currentTimeMillis() / 1000.0;
                System.out.printf("Finished %d in %f seconds%n", total, endTime - startTime);
            }
            return null;
        }
    }
}
