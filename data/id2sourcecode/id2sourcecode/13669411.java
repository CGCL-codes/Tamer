    public void testNIST() throws Exception {
        String p = "NISTXMLSchemaTestSuite.location";
        String v = System.getProperty(p);
        if (v == null || v.length() == 0) {
            System.out.println("System property " + p + " is not set, skipping this test.");
            return;
        }
        URL url = new URL(v);
        url = new URL(url, "NISTXMLSchemaTestSuite.xml");
        InputSource isource = new InputSource(url.openStream());
        isource.setSystemId(url.toString());
        Document document = getDocumentBuilder().parse(isource);
        NodeList links = document.getElementsByTagNameNS(null, "Link");
        for (int i = 0; i < links.getLength(); i++) {
            Element link = (Element) links.item(i);
            runTests(url, link.getAttribute("name"), link.getAttribute("href"));
        }
        System.out.println("Result: Passed = " + numOk + ", Failed = " + numFailed);
    }
