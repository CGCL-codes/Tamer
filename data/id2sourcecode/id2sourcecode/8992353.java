    public void testAttributeWithoutQuotation() {
        StringReader reader = new StringReader(html);
        StringWriter writer = new StringWriter();
        try {
            HTMLParser.process(reader, writer, this, true);
            String buffer = new String(writer.toString());
            System.out.println(buffer);
            assertEquals("<html><head><title>test</title></head><body><img border=\"0px\" width=\"220px\" src=\"test.jpg\"/><p>The image should be there too</p></body></html>", buffer);
        } catch (HandlingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
