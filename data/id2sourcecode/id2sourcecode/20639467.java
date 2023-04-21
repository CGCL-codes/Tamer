    public XPathNodesListFactory(URL url, String xpathExpr) throws IOException {
        this(url.openStream(), xpathExpr);
    }
