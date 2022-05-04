````
## To run:
gradlew build
javac example\nsu\fit\javaperf\TransactionProcessor.java
cd example & jar cvfm TransactionProcessor.jar MANIFEST.MF nsu\fit\javaperf\*.class & cd ..
java -javaagent:build\libs\TransactionProcessorAgent-1.0-SNAPSHOT.jar -jar example\TransactionProcessor.jar
````