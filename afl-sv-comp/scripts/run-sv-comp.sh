#!/bin/bash
#run this script as

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
