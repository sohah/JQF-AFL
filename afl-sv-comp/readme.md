The examples in this folder are meant for debugging purposes. Here are the steps needed to run any of them:

1. go inside the folder of the example you want to run, i.e., StringValueof05
2. compile the test file using the command:
   ```javac -cp ./..:$(../../scripts/classpath.sh) MainTest.java```
3. run jqf-af-fuzzer using the command:
   ```../../bin/jqf-afl-fuzz -v -c .:.. -i ./../../afl/testcases/random MainTest mainTest```
4. run the reproducer using the command:
   ```../../bin/jqf-repro -c .:.. MainTest mainTest ../../afl/testcases/random/random1000.bin ```
