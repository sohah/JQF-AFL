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
