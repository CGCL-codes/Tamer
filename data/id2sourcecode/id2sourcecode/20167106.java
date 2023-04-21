    public TestReport runImpl() throws Exception {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parser);
        File f = (new File(testFileName));
        URL url = f.toURL();
        Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());
        Element e = doc.getElementById(targetId);
        if (e == null) {
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode(ERROR_GET_ELEMENT_BY_ID_FAILED);
            report.addDescriptionEntry(ENTRY_KEY_ID, targetId);
            report.setPassed(false);
            return report;
        }
        try {
            e.removeAttribute(targetAttr);
        } catch (DOMException ex) {
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode(TestReport.ERROR_TEST_FAILED);
            report.addDescriptionEntry("exception.message", ex.getMessage());
            report.setPassed(false);
            return report;
        }
        return reportSuccess();
    }
