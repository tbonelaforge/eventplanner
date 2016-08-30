all: eventplannertest

eventplannertest: EventPlannerTester.class
	@echo "Writing shortcut script eventplannertest..."
	@echo '#! /bin/sh' > eventplannertest
	@echo 'java -cp ../../lib/httpclient-4.5.1.jar:../../lib/httpcore-4.4.4.jar:../../lib/commons-logging-1.2.jar:../ eventplanner.EventPlannerTester' >> eventplannertest
	@chmod +x eventplannertest

EventPlannerTester.class: EventPlannerTester.java EventPlanner.java EventPlannerThread.java GetSystemStatusEvent.java
	javac -cp "../../lib/httpclient-4.5.1.jar:../../lib/httpcore-4.4.4.jar:../" EventPlannerTester.java

EventPlanner.class: EventPlanner.java
	javac -cp ../ EventPlanner.java

GetSystemStatusEvent.class: GetSystemStatusEvent.java
	javac -cp "../../lib/httpclient-4.5.1.jar:../../lib/httpcore-4.4.4.jar:../" GetSystemStatusEvent.java

.PHONY: clean

clean:
	rm -f eventplannertest
	rm *.class
