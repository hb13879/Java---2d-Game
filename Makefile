JCC = ~/Desktop/jdk-9.0.4/bin/javac
JFLAGS =
JVM = ~/Desktop/jdk-9.0.4/bin/java
.SUFFIXES:.java .class

MAIN = Display
TEST = Grid

default: build test run

build:
	$(JCC) $(JFLAGS) Display.java

run: $(MAIN).class
	$(JVM) $(MAIN)

test: $(TEST).class
	$(JVM) $(TEST)

clean:
	$(RM) *.class
