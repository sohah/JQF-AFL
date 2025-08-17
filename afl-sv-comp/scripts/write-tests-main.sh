#!/bin/bash
#run this script as the following from the root of jqf, i.e., jqf-afl-paper
#./afl-sv-comp/scripts/write-tests-main.sh
# assumes a directory structure of
#    bench-defs -> something(git) ->jqf->afl
#                                 ->afl-sv-comp->scripts
#                                              ->sv-comp-false.txt
#

FIND_OPTIONS="-name 'Main.java'"

PWD=$(pwd)
unsafe_tasks=$PWD/afl-sv-comp/sv-comp-false.txt;
while IFS= read -r line || [[ -n "$line" ]]; do
  benchmark_yml="${line##*/}"        # Get 'coral22.yml'
  benchmark="${benchmark_yml%.yml}"       # Remove '.yml'
  echo line=$line
  dirname=$(dirname "$line")
  echo "dirname1=$dirname"
  FIND_OPTIONS="-name 'Main.java'"
  source_path="$PWD/../../bench-defs/sv-benchmarks/java/$dirname/$benchmark"
  files=(`eval "find $source_path $FIND_OPTIONS"`)
  maintTest=${files[0]}
  echo "benchmark=$benchmark"
  echo "dirname=$dirname"
  echo "files=$maintTest"
  echo "PWD=$PWD"
  TEST_FILE_PATH=$(python3 "$PWD"/afl-sv-comp/scripts/class_to_unit_test.py "$maintTest")

#  need to add code here to handle the case where the MinePump needs to add an IOException to
# some of its methods. More precisely, i should check if the filename contains a minepump then we
# should these two replacements to MainTest.java,
# sed -i 's/public static void randomSequenceOfActions(int maxLength) {/public static void randomSequenceOfActions(int maxLength) throws IOException {/' ./*/MainTest.java
# sed -i 's/public static boolean getBoolean() {/public static boolean getBoolean() throws IOException {/' ./*/MainTest.java
# so far I have found it easier to just change them from the terminal than to change the script and run the script
  echo TEST_FILE_PATH="$TEST_FILE_PATH"
done < "$unsafe_tasks"


#Note that for some of the java-ranger verification tasks they do not have the same name pattern as in the remaining of the benchmarks, and thus we have changed their names manually. More precisely, we have ran this script on the sv-comp-false.txt where it has the following names for some of java-ranger benchmarks:
#
#java-ranger-regression/nanoxml_eqchk/nanoxml_prop2.yml
#java-ranger-regression/nanoxml_eqchk/nanoxml_prop3.yml
#java-ranger-regression/printtokens_eqchk/printtokens_prop2.yml
#java-ranger-regression/replace5_eqchk/replace5_prop2.yml
#java-ranger-regression/siena_eqchk/siena_prop2.yml
#java-ranger-regression/WBS/WBS_prop1.yml
#java-ranger-regression/WBS/WBS_prop3.yml
#java-ranger-regression/WBS/WBS_prop4.yml
#
#instead of having these names
#
#java-ranger-regression/nanoxml_eqchk/nanoxml_prop2.yml
#java-ranger-regression/nanoxml_eqchk/nanoxml_prop3.yml
#java-ranger-regression/printtokens_eqchk/printtokens_prop2.yml
#java-ranger-regression/replace5_eqchk/replace5_prop2.yml
#java-ranger-regression/siena_eqchk/siena_prop2.yml
#java-ranger-regression/WBS/WBS_prop1.yml
#java-ranger-regression/WBS/WBS_prop3.yml
#java-ranger-regression/WBS/WBS_prop4.yml
#
#
