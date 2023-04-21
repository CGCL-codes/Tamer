    public void genJavaRead(PrintWriter writer, int depth, String readLabel) {
        genJavaDebugRead(writer, depth, readLabel, "\"\"");
        indent(writer, depth);
        writer.print(readLabel);
        writer.print(" = new ");
        writer.print(name());
        writer.println("(vm, ps);");
    }
