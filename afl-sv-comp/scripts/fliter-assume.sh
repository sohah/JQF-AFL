#!/bin/bash

PWD=$(pwd)
unsafe_tasks=$PWD/afl-sv-comp/sv-comp-false.txt;
while read -r line; do
  benchmark_yml="${line##*/}"        # Get 'coral22.yml'
  benchmark="${benchmark_yml%.yml}"       # Remove '.yml'
  dirname=$(echo "$line" | cut -d'/' -f1)
  source_path="$PWD/../../sv-benchmarks/java/$dirname/$benchmark"
  if ! grep -q "Verifier\.assume" $source_path/Main.java; then
    echo "$line"
  fi
done < "$unsafe_tasks"