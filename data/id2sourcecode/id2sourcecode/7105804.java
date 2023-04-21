    public void genJavaRead(PrintWriter writer, int depth, String readLabel) {
        genJavaDebugRead(writer, depth, readLabel, "\"\"");
        String cntLbl = readLabel + "Count";
        indent(writer, depth);
        writer.println("int " + cntLbl + " = ps.readInt();");
        indent(writer, depth);
        writer.println(readLabel + " = new " + member.javaType() + "[" + cntLbl + "];");
        indent(writer, depth);
        writer.println("for (int i = 0; i < " + cntLbl + "; i++) {;");
        member.genJavaRead(writer, depth + 1, readLabel + "[i]");
        indent(writer, depth);
        writer.println("}");
    }
