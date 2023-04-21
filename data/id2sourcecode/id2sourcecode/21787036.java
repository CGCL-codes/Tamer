    public Document parseXmlUrl(URL url, boolean validating) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        System.out.println("Using document builder factory [" + factory.getClass().getName() + "]");
        factory.setValidating(validating);
        Document doc = factory.newDocumentBuilder().parse(url.openStream());
        return doc;
    }
