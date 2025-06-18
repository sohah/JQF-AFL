#!/bin/bash

#run this script as
#./afl-sv-comp/scripts/alf-svcomp.sh --64 --propertyfile ../../bench-defs/sv-benchmarks/java/properties/assert_java.prp  ../../bench-defs/sv-benchmarks/java/common/ ../../bench-de
 #fs/sv-benchmarks/java/jbmc-regression/boolean1

# parse arguments
declare -a BM
BM=()
PROP_FILE=""
WITNESS_FILE=""

FIND_OPTIONS="-name 'Main.java'"

while [ -n "$1" ] ; do
case "$1" in
  --32|--64) BIT_WIDTH="${1##--}" ; shift 1 ;;
  --propertyfile) PROP_FILE="$2" ; shift 2 ;;
  --graphml-witness) WITNESS_FILE="$2" ; shift 2 ;;
  --version) echo 2025; exit 0 ;;
  *) SRC=(`eval "find $1 $FIND_OPTIONS"`) ; BM=("${BM[@]}" "${SRC[@]}") ; shift 1 ;;
esac
done

echo "SRC obtained is = ${SRC[0]}"
#depth_limit=$1
#date -r java-ranger/build/jpf-symbc.jar

if [ -z "${BM[0]}" ] || [ -z "$PROP_FILE" ] ; then
echo "Missing benchmark or property file"
exit 1
fi

if [ ! -s "${BM[0]}" ] || [ ! -s "$PROP_FILE" ] ; then
echo "Empty benchmark or property file"
exit 1
fi

SRC_0=${SRC[0]}
PWD=$(pwd)
echo "PROP_FILE=$PROP_FILE"
echo SRC="$SRC_0"
echo "PWD=$PWD"

parent_dir=$(dirname "$SRC_0")         # => .../boolean1
BENCHMARK_NAME=$(basename "$parent_dir")

#LOG=`mktemp -t jpf-log.XXXXXX`
#DIR=`mktemp -d -t jpf-benchmark.XXXXXX`
DIR="$PWD/sv-comp-log/$(date +'%m-%d-%y-%Hh-%Mm')/$BENCHMARK_NAME"
mkdir -p "$DIR"
SHELL_LOG=$DIR/shellscript.log
echo "TEMP_DIR=$DIR"
#trap "rm -rf $DIR" EXIT

# create target directory
mkdir -p $DIR/target/classes


TEST_FILE_PATH=$(python3 /media/soha/aac0ad21-227d-4952-81c1-3117f40e4379/home/oem/git/jqf-afl-paper/afl-sv-comp/scripts/class_to_unit_test.py "$SRC_0")

# build src files from benchmark
echo TEST_FILE_PATH="$TEST_FILE_PATH"
echo javac --release 11 -g -cp "./afl-sv-comp/:$(./scripts/classpath.sh)" -d $DIR/target/classes "$TEST_FILE_PATH" > "$SHELL_LOG"

javac --release 11 -g -cp "./afl-sv-comp/:$(./scripts/classpath.sh)" -d $DIR/target/classes "$TEST_FILE_PATH" >> "$SHELL_LOG"

export AFL_DIR=$PWD/afl

echo "\n\nrunning afl-now ....\n\n"

cd $DIR && $AFL_DIR/../bin/jqf-afl-fuzz -v -c $DIR/target/classes:./afl-sv-comp/ -i $AFL_DIR/testcases/random/ MainTest mainTest >> "$SHELL_LOG"
