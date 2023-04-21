    protected void generateReadMethod(NodeClass c, TabPrintWriter writer) {
        writer.startLine("private " + c.name() + " read" + upperCaseFirst(c.name()) + "Body()");
        writer.print(" throws java.io.IOException {");
        writer.indent();
        for (Field f : c.allFields(ast)) {
            writer.startLine("readFieldDelim(\"" + f.name() + " = \");");
            Pair<String, Boolean> readVal = elementReadString(f.type(), true);
            if (readVal.second()) {
                writer.startLine("@SuppressWarnings(\"unchecked\") ");
            } else {
                writer.startLine();
            }
            writer.print(f.type().name() + " read_" + f.name() + " = " + readVal.first() + ";");
        }
        writer.startLine("return new " + c.name() + "(");
        boolean first = true;
        for (Field f : c.allFields(ast)) {
            if (first) {
                first = false;
            } else {
                writer.print(", ");
            }
            writer.print("read_" + f.name());
        }
        writer.print(");");
        writer.unindent();
        writer.startLine("}");
        writer.println();
    }
