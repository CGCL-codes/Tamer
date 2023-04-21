    @Test
    public void testProviderJava2Html() throws IOException {
        File userDir = null;
        Properties java2HtmlConfig = null;
        String conversionType = "html";
        Java2HtmlRunner runner = new Java2HtmlRunner(userDir, java2HtmlConfig, null);
        StringReader reader = new StringReader(getJavaExample());
        StringWriter writer = new StringWriter();
        runner.execute(reader, writer, conversionType, java2HtmlConfig);
        System.out.println(writer.toString());
    }
