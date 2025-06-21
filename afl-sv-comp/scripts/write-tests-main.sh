#!/bin/bash
#run this script as the following from the root of jqf, i.e., jqf-afl-paper
#./afl-sv-comp/scripts/run-sv-comp.sh
# assumes a directory structure of
#    bench-defs -> something(git) ->jqf->afl
#                                 ->afl-sv-comp->scripts
#                                              ->sv-comp-false.txt
#

FIND_OPTIONS="-name 'Main.java'"

PWD=$(pwd)
unsafe_tasks=$PWD/afl-sv-comp/sv-comp-false.txt;
while read -r line; do
  benchmark_yml="${line##*/}"        # Get 'coral22.yml'
  benchmark="${benchmark_yml%.yml}"       # Remove '.yml'
  dirname=$(echo "$line" | cut -d'/' -f1)
  FIND_OPTIONS="-name 'Main.java'"
  source_path="$PWD/../../bench-defs/sv-benchmarks/java/$dirname/$benchmark"
  files=(`eval "find $source_path $FIND_OPTIONS"`)
  maintTest=${files[0]}
  echo "benchmark=$benchmark"
  echo "dirname=$dirname"
  echo "files=$maintTest"
  echo "PWD=$PWD"
  TEST_FILE_PATH=$(python3 "$PWD"/afl-sv-comp/scripts/class_to_unit_test.py "$maintTest")

  echo TEST_FILE_PATH="$TEST_FILE_PATH"
done < "$unsafe_tasks"


# to reproduce run a command similar to
# ./bin/jqf-repro -c .:./afl-sv-comp/:/tmp/jpf-benchmark.fP6ldG/target/classes MainTest mainTest fuzz-results/queue/id\:000000\,orig\:rand100.bin