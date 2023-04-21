    private static Document fetchAsDom(URL url) throws MalformedURLException, IOException {
        Tidy tidy = new Tidy();
        tidy.setXHTML(false);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        InputStream in = url.openStream();
        Document dom = tidy.parseDOM(in, null);
        in.close();
        return dom;
    }
