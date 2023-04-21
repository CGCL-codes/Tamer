    public void generateClassMembers(TabPrintWriter writer, NodeClass c) {
        if (ast.isTopClass(c)) {
            writer.startLine("/** Generate a human-readable representation that can be deserialized. */");
            writer.startLine("public java.lang.String serialize() {");
            writer.indent();
            writer.startLine("java.io.StringWriter w = new java.io.StringWriter();");
            writer.startLine("walk(new LosslessStringWalker(w, 2));");
            writer.startLine("return w.toString();");
            writer.unindent();
            writer.startLine("}");
            writer.startLine("/** Generate a human-readable representation that can be deserialized. */");
            writer.startLine("public void serialize(java.io.Writer writer) {");
            writer.indent();
            writer.startLine("walk(new LosslessStringWalker(writer, 2));");
            writer.unindent();
            writer.startLine("}");
            writer.println();
        }
    }
