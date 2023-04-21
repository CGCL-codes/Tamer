    public static DefaultMutableTreeNode parse(URL url, HelpSet hs, Locale locale, TreeItemFactory factory, TOCView view) {
        Reader src;
        DefaultMutableTreeNode node = null;
        try {
            URLConnection uc = url.openConnection();
            src = XmlReader.createReader(uc);
            factory.parsingStarted(url);
            TOCParser tocParser = new TOCParser(factory, view);
            node = (tocParser.parse(src, hs, locale));
            src.close();
        } catch (Exception e) {
            factory.reportMessage("Exception caught while parsing " + url + e.toString(), false);
        }
        return factory.parsingEnded(node);
    }
