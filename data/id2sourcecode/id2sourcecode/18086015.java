    private Document wrapHTMLtoXHTML() throws IOException {
        Document doc = null;
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);
        tidy.setXHTML(true);
        tidy.setAltText("none");
        tidy.setOnlyErrors(true);
        tidy.setShowWarnings(false);
        tidy.setInputEncoding("utf-8");
        doc = tidy.parseDOM(url.openStream(), null);
        return doc;
    }
