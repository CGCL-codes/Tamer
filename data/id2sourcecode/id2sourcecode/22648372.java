    public void parseInto(URL url, HelpSetFactory factory) {
        Reader src;
        try {
            URLConnection uc = url.openConnection();
            src = XmlReader.createReader(uc);
            factory.parsingStarted(url);
            (new HelpSetParser(factory)).parseInto(src, this);
            src.close();
        } catch (Exception ex) {
            factory.reportMessage("Got an IOException (" + ex.getMessage() + ")", false);
            if (debug) ex.printStackTrace();
        }
        for (int i = 0; i < subHelpSets.size(); i++) {
            HelpSet subHS = (HelpSet) subHelpSets.elementAt(i);
            add(subHS);
        }
    }
