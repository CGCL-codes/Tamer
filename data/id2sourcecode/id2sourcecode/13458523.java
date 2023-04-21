    public static DefaultMutableTreeNode parse(URL url, HelpSet hs, Locale locale, TreeItemFactory factory) {
        Reader src;
        DefaultMutableTreeNode node = null;
        try {
            URLConnection uc = url.openConnection();
            src = XmlReader.createReader(uc);
            factory.parsingStarted(url);
            node = (new IndexParser(factory)).parse(src, hs, locale);
            src.close();
        } catch (Exception e) {
            factory.reportMessage("Exception caught while parsing " + url + e.toString(), false);
        }
        return factory.parsingEnded(node);
    }
