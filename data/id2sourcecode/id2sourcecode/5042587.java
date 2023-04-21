    @Test(groups = "unit")
    public void testXPathReplacementNamespace() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        this.reader = new StringReader(TEST_NAMESPACE_XML_INPUT);
        String xpath = "/pref:country/pref:language/text()";
        Map<String, String> prefixToNamespace = new HashMap<String, String>();
        prefixToNamespace.put("pref", "http://example.com/country");
        XPathReplacement.replace(xpath, prefixToNamespace, REPLACEMENT_VALUE, this.reader, this.writer);
        String result = this.writer.toString();
        assertTrue(result.contains(REPLACEMENT_VALUE));
        assertFalse(result.contains(REPLACED_VALUE));
    }
