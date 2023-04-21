    public void testJava() {
        Reader configReader = null;
        Reader reader = null;
        Writer writer = null;
        try {
            configReader = new InputStreamReader(getClass().getResourceAsStream("/java.properties"));
            reader = new InputStreamReader(getClass().getResourceAsStream("/test.java"));
            writer = new OutputStreamWriter(TestUtils.getOutputStream("java.html"));
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
