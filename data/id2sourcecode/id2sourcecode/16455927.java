    public void generateInterfaceMembers(TabPrintWriter writer, NodeInterface i) {
        if (ast.isTop(i)) {
            writer.startLine("/** Generate a human-readable representation that can be deserialized. */");
            writer.startLine("public java.lang.String serialize();");
            writer.startLine("/** Generate a human-readable representation that can be deserialized. */");
            writer.startLine("public void serialize(java.io.Writer writer);");
        }
    }
