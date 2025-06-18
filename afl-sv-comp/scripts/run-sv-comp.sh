#!/bin/bash
#run this script as the following from the root of jqf, i.e., jqf-afl-paper
#./afl-sv-comp/scripts/run-sv-comp.sh
# assumes a directory structure of
#    bench-defs -> something(git) ->jqf->afl
#                                 ->afl-sv-comp->scripts
#                                              ->sv-comp-false.txt
#

DURATION=800

PWD=$(pwd)
unsafe_tasks=$PWD/afl-sv-comp/sv-comp-false.txt;
while read -r line; do
  benchmark_yml="${line##*/}"        # Get 'coral22.yml'
  benchmark="${benchmark_yml%.yml}"       # Remove '.yml'
  dirname=$(echo "$line" | cut -d'/' -f1)
  echo "$dirname"

  timeout "$DURATION"s ./afl-sv-comp/scripts/alf-svcomp.sh --64 --propertyfile $PWD/../../bench-defs/sv-benchmarks/java/properties/assert_java.prp  $PWD/../../bench-defs/sv-benchmarks/java/common/ $PWD/../../bench-defs/sv-benchmarks/java/"$dirname"/"$benchmark"
done < "$unsafe_tasks"


# to reproduce run a command similar to
# ./bin/jqf-repro -c .:./afl-sv-comp/:/tmp/jpf-benchmark.fP6ldG/target/classes MainTest mainTest fuzz-results/queue/id\:000000\,orig\:rand100.bin