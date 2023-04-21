    private void generateResponseReaderMethod(SourceWriter writer, TypeOracle typeOracle, JType returnType, JClassType jsoClass) {
        writer.println("public " + returnType.getSimpleSourceName() + " read(String response) {");
        writer.indent();
        writer.println("return " + jsoClass.getSimpleSourceName() + ".create(response);");
        writer.outdent();
        writer.println("}");
    }
