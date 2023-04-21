    private void readRows() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        _builder = factory.newDocumentBuilder();
        URL url = new URL(_xmlResourceName);
        InputStream is = url.openStream();
        Document doc = _builder.parse(is);
        Element root = doc.getDocumentElement();
        _rawRows = root.getElementsByTagName(_rowTag);
        _rowCount = _rawRows.getLength();
        is.close();
    }
