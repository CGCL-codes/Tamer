    protected void testExecute(String html, String result) {
        StringReader reader = new StringReader(html);
        StringWriter writer = new StringWriter();
        try {
            HTMLParser.process(reader, writer, new XSSFilter(), true);
            String buffer = new String(writer.toString());
            System.out.println(buffer);
            assertEquals(result, buffer);
        } catch (HandlingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
