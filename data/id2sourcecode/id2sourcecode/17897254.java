    public TestReport runImpl() throws Exception {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parser);
        File f = (new File(testFileName));
        URL url = f.toURL();
        Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());
        Element root = doc.getDocumentElement();
        NodeList lst = root.getElementsByTagNameNS(null, tagName);
        if (lst.getLength() != 1) {
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode("error.getElementByTagNameNS.failed");
            report.setPassed(false);
            return report;
        }
        Node n;
        while ((n = root.getFirstChild()) != null) {
            root.removeChild(n);
        }
        if (lst.getLength() != 0) {
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode("error.getElementByTagNameNS.failed");
            report.setPassed(false);
            return report;
        }
        return reportSuccess();
    }
