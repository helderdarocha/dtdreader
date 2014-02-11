REM JUnit location - EDIT THIS LINE
set JUNITJAR=c:/dev/junit/junit.jar

cd build
java -cp %JUNITJAR%;. junit.swingui.TestRunner argonavis.dtd.AllTests