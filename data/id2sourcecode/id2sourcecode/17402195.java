    public void genJavaRead(PrintWriter writer, int depth, String readLabel) {
        indent(writer, depth);
        writer.print(readLabel);
        writer.print(" = ");
        writer.print(javaRead());
        writer.println(";");
        genJavaDebugRead(writer, depth, readLabel, debugValue(readLabel));
    }
