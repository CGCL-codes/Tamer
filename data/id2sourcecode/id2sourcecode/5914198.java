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
        Element fc = null;
        for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                fc = (Element) n;
                break;
            }
        }
        Element ne = doc.createElementNS(null, "elt4");
        e.replaceChild(ne, fc);
        if (ne.getParentNode() != e || fc.getParentNode() != null) {
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode(TestReport.ERROR_TEST_FAILED);
            report.setPassed(false);
            return report;
        }
        return reportSuccess();
    }
