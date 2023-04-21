    public FavoritesNode parse(HelpSet hs, Locale locale, TreeItemFactory factory) {
        Reader src;
        DefaultMutableTreeNode node = null;
        URL url = null;
        try {
            String user_dir = System.getProperty("user.home");
            File file = new File(user_dir + File.separator + ".JavaHelp" + File.separator + "Favorites.xml");
            if (!file.exists()) return new FavoritesNode(new FavoritesItem("Favorites"));
            try {
                url = file.toURL();
            } catch (MalformedURLException e) {
                System.err.println(e);
            }
            URLConnection uc = url.openConnection();
            src = XmlReader.createReader(uc);
            factory.parsingStarted(url);
            node = (new FavoritesParser(factory)).parse(src, hs, locale);
            src.close();
        } catch (Exception e) {
            factory.reportMessage("Exception caught while parsing " + url + e.toString(), false);
        }
        return (FavoritesNode) factory.parsingEnded(node);
    }
