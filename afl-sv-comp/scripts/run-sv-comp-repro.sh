#!/bin/bash
#run this script as the following from the root of jqf, i.e., jqf-afl-paper
#./afl-sv-comp/scripts/run-sv-comp.sh
# assumes a directory structure of
#    bench-defs -> something(git) ->jqf->afl
#                                 ->afl-sv-comp->scripts
#                                              ->sv-comp-false.txt
#

ORIG_PWD=$(pwd)
unsafe_tasks=$ORIG_PWD/afl-sv-comp/sv-comp-repro.txt;
while read -r line; do
  benchmark_yml="${line##*/}"        # Get 'coral22.yml'
  benchmark="${benchmark_yml%.yml}"       # Remove '.yml'
  dirname=$(echo "$line" | cut -d'/' -f1)
  dirname_path="$ORIG_PWD/sv-comp-log/$benchmark"
  echo "$dirname_path"

  echo running reproducability for "$benchmark"
  for crashfile in "$dirname_path"/fuzz-results/crashes/*; do
    # Skip README.txt
    if [[ "$(basename "$crashfile")" == "README.txt" ]]; then
        continue
    fi
    echo "running crash for file $crashfile"
    jqf_repro_shell=$ORIG_PWD/bin
    echo "jqf_repro_shell=$jqf_repro_shell"
    cd "$dirname_path/target/classes" || exit 1
    echo "$jqf_repro_shell/jqf-repro" -c .:.. MainTest mainTest "$crashfile"
    "$jqf_repro_shell/jqf-repro" -c .:.. MainTest mainTest "$crashfile"
  done
  cd "$ORIG_PWD" || exit 1
done < "$unsafe_tasks"

