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
repro_result=$ORIG_PWD/repro_result.log
echo > "$repro_result"
while IFS= read -r line || [[ -n "$line" ]]; do
  benchmark_yml="${line##*/}"        # Get 'coral22.yml'
  benchmark="${benchmark_yml%.yml}"       # Remove '.yml'
  dirname=$(echo "$line" | cut -d'/' -f1)
  dirname_path="$ORIG_PWD/sv-comp-log/$benchmark"
  echo "$dirname_path"

  echo running reproducability for "$benchmark"


  crash_dir="$dirname_path/fuzz-results/crashes"
  repro_log=$dirname_path/repro.log
  echo > "$repro_log"
  # Count crash files excluding README.txt
  crash_count=$(find "$crash_dir" -type f ! -name "README.txt" | wc -l)
  jqf_repro_shell="$ORIG_PWD/bin"
  echo "jqf_repro_shell=$jqf_repro_shell"

  if (( crash_count > 0 )); then
      for crashfile in "$crash_dir"/*; do
          if [[ "$(basename "$crashfile")" == "README.txt" ]]; then
              continue
          fi
          echo "running crash for file $crashfile"
          cd "$dirname_path/target/classes" || exit 1
          echo "$jqf_repro_shell/jqf-repro" -c .:.. MainTest mainTest "$crashfile"
          timeout 300s "$jqf_repro_shell/jqf-repro" -c .:.. MainTest mainTest "$crashfile" | tee -a "$repro_log"
      done
  else
      echo "No crash files to process."
      echo "first attempting running on the seed"
      cd "$dirname_path/target/classes" || exit 1
      timeout 300s "$jqf_repro_shell/jqf-repro" -c .:.. MainTest mainTest "../../random/random1000.bin" | tee -a "$repro_log"
  fi

  grep "java.lang.AssertionError" "$repro_log" > /dev/null
  if [ $? -eq 0 ]; then
    echo "$line, UNSAFE" | tee -a "$repro_result"
  else
    echo "$line, UNKNOWN" | tee -a "$repro_result"
  fi

  cd "$ORIG_PWD" || exit 1
done < "$unsafe_tasks"

