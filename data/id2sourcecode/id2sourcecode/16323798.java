    public void testC() {
        Reader configReader = null;
        Reader reader = null;
        Writer writer = null;
        try {
            configReader = new InputStreamReader(getClass().getResourceAsStream("/c.properties"));
            reader = new InputStreamReader(getClass().getResourceAsStream("/test.c"));
            writer = new OutputStreamWriter(TestUtils.getOutputStream("c.html"));
            Configuration config = new Configuration();
            config.load(configReader);
            CodeSnippetGenerator generator = new CodeSnippetGenerator(config);
            generator.generate(reader, writer);
            writer.flush();
        } catch (Exception e) {
            if (configReader != null) {
                try {
                    configReader.close();
                } catch (IOException e1) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            }
        }
    }
