# JQF + AFL for verification

This repository contains a set of tools to run JQF with AFL for verification purposes. In particular this repository is used to run jqf-afl on sv-comp benchmarks.  
Important notes about usage of this repo, where DIR refers to the top-level directory of this repository:

1. need to install afl under DIR, so that DIR/afl is the afl directory
2. the main sv-comp script is called [jqf-afl-svcomp.sh](jqf-afl-svcomp.sh), which is located in DIR
3. the directory DIR/afl-svcomp contains some examples from sv-comp benchmarks plus
     - [java-sv-benchmarks](afl-sv-comp/java-sv-benchmarks) have a version of the sv-comp benchmarks that have a corresponding Test class for them. These are usually the output running the [write-tests-main.sh](afl-sv-comp/scripts/write-tests-main.sh) script, however, a few were manually modified.
     - `org` folder that contains the implementation for the `Verifier` interface
     - [sv-comp-false.txt](afl-sv-comp/sv-comp-false.txt) contains the list unsafe tasks in sv-comp. These are of course a subset of the entire suite of sv-comp benchmarks.
     - [sv-comp-repro.txt](afl-sv-comp/sv-comp-repro.txt) which contains of tasks for which we want to run reproducability for.
     - [write-tests-main.sh](afl-sv-comp/scripts/write-tests-main.sh) which uses the [class_to_unit_test.py](afl-sv-comp/scripts/class_to_unit_test.py) to generate a unit test for the sv-comp benchmarks. This script is used to generate the unit tests for the sv-comp benchmarks.
     - [run-sv-comp-repro.sh](afl-sv-comp/scripts/run-sv-comp-repro.sh) is used to run reproducibility on sv-comp benchmarks for which we have some results for. It uses the crashed input if one is found, otherwise it uses the random input that is used as a default.
     - [fliter-assume.sh](afl-sv-comp/scripts/fliter-assume.sh), this is unused script, which we used in initial runs of experiments, were we selected benchmarks that do not have an assume statement in their code. But now, as we have supported assume statements, this script is not used anymore.



## Running jqf-afl on sv-comp benchmarks

We will call the root of the benchexec found [here](https://gitlab.com/sosy-lab/sv-comp/bench-defs), BENCH_DEFS. 
1. To run jqf-afl on sv-comp benchmarks, you need to copy the entire directory DIR TO BENCH_DEF/archives, make sure that this copy does not include either [expirement-logs](afl-sv-comp/expirement-logs), or [java-sv-benchmarks](afl-sv-comp/java-sv-benchmarks). As these should not be part of the binary.
2. copy the contents of [java-sv-benchmarks](afl-sv-comp/java-sv-benchmarks) to those under BENCH_DEFS/java-sv-benchmarks.
3. You need of course to update the tool definition as well you need to make the appropriate changes to the tool `https://gitlab.com/sosy-lab/benchmarking/fm-tools` but this repo should be placed separately outside the BENCH_DEFS directory, one suggestion it to make it a sibling directory to BENCH_DEFS. 
4. Use the command ``` scripts/execute_runs/execute-runcollection.sh                                                                            benchexec/bin/benchexec                                                                            archives/jqf-afl.zip                                                                            benchmark-defs/jqf-afl.xml                                                                            witness.graphml                                                                            results-verified/                                                                            -t ReachSafety-Java                                                                            --timelimit 900 --memorylimit 3GB --limitCores 1 --numOfThreads 1                                                                           --read-only-dir / --overlay-dir /home/ --full-access-dir ./```
5. You can find the output generate under `BENCH_DEFS/results-verified/`. And you can find the actually binary directory that was unziped and ran under BENCH_DEFS/bin with a pattern name like `jqf-afl-RANDOM`.
6. You can find the log files for jqf under `BENCH_DEFS/bin/jqf-afl-RANDOM>/sv-comp-log/`. 
7. To run the reproducibility script, you need to open the terminal so that you are inside `BENCH_DEFS/bin/jqf-afl-RANDOM>`. Also, you need to ensure that the  `BENCH_DEFS/bin/jqf-afl-RANDOM/afl-sv-comp/sv-comp-repro.txt` file contains the list of benchmarks for which you want to run the reproducibility for. Then you can type `./afl-sv-comp/scripts/run-sv-comp-repro.sh` assuming that the script is executable, if not, then you need to make it executable by running `chmod +x ./afl-sv-comp/scripts/run-sv-comp-repro.sh`. 
8. The output of the reproducibility script will be found under `BENCH_DEFS/bin/jqf-afl-RANDOM/afl-sv-comp/repro-result.log`.
