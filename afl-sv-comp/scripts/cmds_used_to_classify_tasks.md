To classify the verification tasks into two disjoint sets, safe and unsafe reachsafety tasks, we did the following at the root of `sv-benchmarks` directory of `sv-comp`::
1. I copied all the tasks into `temp-false` directory, that contains all the unsafe tasks yml files with the second entry of the `expected_verdict` removed. This is because the second occurrence for it is actually for the runtime-exception, which we want to ignore. For this copy, I used the following script:
```bash
#!/bin/bash

# Create output directory
outdir="./temp-false"
mkdir -p "$outdir"

# Recursively process all .yml files
find . -type f -name "*.yml" | while read -r file; do
    # Remove leading './' for cleaner path building
    relpath="${file#./}"

    # Build destination path inside temp-false
    dest="$outdir/$relpath"

    # Ensure subdirectories exist in the destination
    mkdir -p "$(dirname "$dest")"

    # Process file and remove second occurrence of 'expected_verdict'
    awk '
        /expected_verdict/ {
            count++
            if (count == 2) next  # skip second occurrence
        }
        { print }
    ' "$file" > "$dest"

    echo "Processed: $file -> $dest"
done
   ```
2. At this point, everything was copied, but I also found there is a faulty copy happening at `rtems-lock-model`, which I am not really sure out why is it happening, but I fixed it manually by removing that location.
3. next I ran the following to create three files that contains all tasks, all safe tasks and all unsafe tasks:
```bash
find . -type f -name "*.yml" > sv-comp-all.txt 
```

```bash
grep -r --include="*.yml" " true" . | sort -u > sv-comp-true.txt 
```

```bash
grep -r --include="*.yml" " false" . | sort -u > sv-comp-false.txt
```
