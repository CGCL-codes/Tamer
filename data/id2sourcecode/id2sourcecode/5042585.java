    @Test(groups = "unit")
    public void testXPathReplacement() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        this.reader = new StringReader(TEST_XML_INPUT);
        String xpath = "/country/language/text()";
        XPathReplacement.replace(xpath, REPLACEMENT_VALUE, this.reader, this.writer);
        String result = this.writer.toString();
        assertTrue(result.contains(REPLACEMENT_VALUE));
        assertFalse(result.contains(REPLACED_VALUE));
    }
