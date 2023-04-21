    private void compileOptionsSchema() throws IOException {
        EXISchema corpus = null;
        try {
            EXISchemaFactoryErrorMonitor compilerErrorHandler = new EXISchemaFactoryErrorMonitor();
            EXISchemaFactory schemaFactory = new EXISchemaFactory();
            schemaFactory.setCompilerErrorHandler(compilerErrorHandler);
            InputSource inputSource;
            URL url = CompileSchemas.class.getResource(OPTIONS_SHEMA_INSTANCE);
            inputSource = new InputSource(url.openStream());
            inputSource.setSystemId(url.toString());
            corpus = schemaFactory.compile(inputSource);
            Assert.assertEquals(0, compilerErrorHandler.getTotalCount());
            Assert.assertNotNull(corpus);
        } catch (Exception exc) {
            Assert.fail("Failed to compile EXI Header Options schema.");
        }
        URL xbrlSchemaURI = CompileSchemas.class.getResource(OPTIONS_SHEMA_INSTANCE);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        URL url = new URL(xbrlSchemaURI, OPTIONS_SCHEMA_COMPILED);
        try {
            fos = new FileOutputStream(url.getFile());
            oos = new ObjectOutputStream(fos);
            oos.writeObject(corpus);
            oos.flush();
            fos.flush();
        } finally {
            if (oos != null) oos.close();
            if (fos != null) fos.close();
        }
    }
