#!/bin/bash

#run this script as
#./afl-sv-comp/scripts/alf-svcomp.sh --64 --propertyfile ../../bench-defs/sv-benchmarks/java/properties/assert_java.prp  ../../bench-defs/sv-benchmarks/java/common/ ../../bench-de
 #fs/sv-benchmarks/java/jbmc-regression/boolean1

DURATION=30

# parse arguments
declare -a BM
BM=()
PROP_FILE=""
WITNESS_FILE=""

FIND_OPTIONS="-name '*.java'"

while [ -n "$1" ] ; do
case "$1" in
  --32|--64) BIT_WIDTH="${1##--}" ; shift 1 ;;
  --propertyfile) PROP_FILE="$2" ; shift 2 ;;
  --graphml-witness) WITNESS_FILE="$2" ; shift 2 ;;
  --version) echo 2025; exit 0 ;;
  *) SRC=(`eval "find $1 $FIND_OPTIONS"`) ; BM=("${BM[@]}" "${SRC[@]}") ; shift 1 ;;
esac
done


if [ -z "${BM[0]}" ] || [ -z "$PROP_FILE" ] ; then
echo "Missing benchmark or property file"
exit 1
fi

if [ ! -s "${BM[0]}" ] || [ ! -s "$PROP_FILE" ] ; then
echo "Empty benchmark or property file"
exit 1
fi

for MainTestFile in "${BM[@]}"; do
  if [[ "$MainTestFile" == *MainTest.java ]]; then
    break
  fi
done

filtered=()
for file in "${BM[@]}"; do
  if [[ "$file" != *Main.java && "$file" != *Verifier.java ]]; then
    filtered+=("$file")
  fi
done

BM=("${filtered[@]}")

PWD=$(pwd)
echo "PROP_FILE=$PROP_FILE"
echo "BM=${BM[*]}"
echo "PWD=$PWD"

parent_dir=$(dirname "$MainTestFile")         # => .../boolean1
BENCHMARK_NAME=$(basename "$parent_dir")

#LOG=`mktemp -t jpf-log.XXXXXX`
#DIR=`mktemp -d -t jpf-benchmark.XXXXXX`
#TIMESTAMP=$(date +'%m-%d-%y-%Hh')
DIR="$PWD/sv-comp-log/$BENCHMARK_NAME"
mkdir -pv "$DIR"
SHELL_LOG=$DIR/shellscript.log
echo "TEMP_DIR=$DIR"
echo "SHELL_LOG=$SHELL_LOG"

# create target directory
mkdir -pv $DIR/target/classes
#trap "rm -rf $DIR" EXIT

classpath=$(./scripts/classpath.sh)
echo javac --release 11 -g -cp "$PWD/afl-sv-comp/:$classpath" -d $DIR/target/classes "${BM[@]}" $PWD/afl-sv-comp/org/sosy_lab/sv_benchmarks/Verifier.java

javac --release 11 -g -cp "$PWD/afl-sv-comp/:$classpath" -d $DIR/target/classes "${BM[@]}" $PWD/afl-sv-comp/org/sosy_lab/sv_benchmarks/Verifier.java #>> "$SHELL_LOG"

export AFL_DIR=$PWD/afl

echo "running afl-now ...."

cd $DIR && timeout "$DURATION" $AFL_DIR/../bin/jqf-afl-fuzz -v -c "$DIR"/target/classes:"$AFL_DIR"/../afl-sv-comp/ -i "$AFL_DIR"/testcases/random/ MainTest mainTest >> "$SHELL_LOG"

grep "java.lang.AssertionError" "$DIR"/jqf.log> /dev/null
if [ $? -eq 0 ]; then
  echo "UNSAFE"
else
  echo "UNKNOWN"
fi