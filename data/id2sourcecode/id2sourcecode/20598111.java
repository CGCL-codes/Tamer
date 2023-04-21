    private Source[] getSources() throws IOException {
        List<Source> result = new LinkedList<Source>();
        SchemaGenerator schemaGenerator = new SchemaGenerator(JAXB_CONTEXT, getNamespaceURISchemaFilenameMap());
        schemaGenerator.generateSchema();
        ClassLoader classloader = getClass().getClassLoader();
        for (File schemafile : schemaGenerator.getGeneratedSchemafiles()) {
            FileUtils.copyFile(schemafile, new File("./bin/" + schemafile.getName()));
            result.add(new StreamSource(classloader.getResourceAsStream(schemafile.getName())));
        }
        return result.toArray(new Source[] {});
    }
