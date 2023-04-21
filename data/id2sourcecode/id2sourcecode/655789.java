    public static final void main(String[] args) throws Exception {
        URL url = new URL("http://example.com");
        Parser p = new Parser();
        SAX2DOM sax2dom = new SAX2DOM();
        p.setContentHandler(sax2dom);
        p.parse(new InputSource(url.openStream()));
        Node doc = sax2dom.getDOM();
        String titlePath = "/html:html/html:head/html:title";
        XObject title = XPathAPI.eval(doc, titlePath);
        System.out.println("Title is '" + title + "'");
    }
