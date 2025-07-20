import sys
import re
import os


def transform_java_class(input_file, new_name):
    # Read original Java file
    with open(input_file, 'r') as f:
        lines = f.readlines()

    # Define the new filename
    output_file = os.path.join(os.path.dirname(input_file), f"{new_name}.java")

    # Prepare import block
    import_block = [
        "import java.io.ByteArrayInputStream;\n",
        "import java.io.ByteArrayOutputStream;\n",
        "import org.sosy_lab.sv_benchmarks.Verifier;\n",
        "import java.io.FileOutputStream;\n",
        "import java.io.IOException;\n",
        "import java.io.InputStream;\n",
        "import edu.berkeley.cs.jqf.fuzz.Fuzz;\n",
        "import edu.berkeley.cs.jqf.fuzz.JQF;\n",
        "import org.junit.runner.RunWith;\n",
        "\n"
    ]

    transformed_lines = []
    class_renamed = False
    imports_inserted = False
    runwith_inserted = False
    inside_main = False
    inserted_verifier_line = False

    for line in lines:
        stripped = line.strip()

        # Insert imports at the top (only once)
        if not imports_inserted and (
            stripped.startswith("package ") or stripped.startswith("import ") or stripped == ""):
            transformed_lines.append(line)
            continue
        elif not imports_inserted:
            transformed_lines = import_block + transformed_lines
            imports_inserted = True

        # Rename the class
        if not class_renamed and re.search(r"^(?:public\s+)?(?:static\s+)?class\s+Main\b", stripped):
            if not runwith_inserted:
                transformed_lines.append("@RunWith(JQF.class)\n")
                runwith_inserted = True
                if("public" in line):
                    line = line.replace("Main", new_name)
                else:
                    line = re.sub(r"\s*class\s+Main\b", "public class " + new_name, line)
            class_renamed = True

        # Transform the main method signature
        if re.match(r"public\s+static\s+void\s+main\s*\(\s*String\s+\w+\[\]\s*\)\s*\{", stripped) or \
            re.match(r"public\s+static\s+void\s+main\s*\(\s*String\s*\[\]\s+\w+\s*\)\s*\{?", stripped):
            transformed_lines.append("    @Fuzz\n")
            line = "    public void mainTest(InputStream input) throws IOException {\n"
            inside_main = True

        if(re.match( r'^(\s*)Main\s+(\w+)\s*=\s*new\s+Main\(\);', stripped)):
            pattern = r'^(\s*)Main\s+(\w+)\s*=\s*new\s+Main\(\);'
            replacement = r'\1MainTest \2 = new MainTest();'
            line = re.sub(pattern, replacement, line)

        transformed_lines.append(line)

        # Insert verifier line after the new method signature
        if inside_main and not inserted_verifier_line and '{' in line:
            transformed_lines.append("    Verifier.input = input;\n")
            inserted_verifier_line = True
            inside_main = False  # Avoid inserting again

    # Write the modified content to the new file
    with open(output_file, 'w') as f:
        f.writelines(transformed_lines)

    # print(f"Transformed file written to {output_file}")
    print(output_file)


# Usage: python script.py Main.java NewName
if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script.py Main.java")
        sys.exit(1)

    input_file = sys.argv[1]
    new_class_name = "MainTest"
    print("\nprinting inputs:\n","input_file=",input_file, "\nnew_class_name=",new_class_name)
    transform_java_class(input_file, new_class_name)
