    private void parseTOC() {
        HelpModel helpModel = treenav.getModel();
        if (helpModel == null) {
            return;
        }
        HelpSet hs = helpModel.getHelpSet();
        NavigatorView view = treenav.getNavigatorView();
        debug("parseTOC - " + view.getName());
        Hashtable params = view.getParameters();
        URL url;
        try {
            url = new URL(hs.getHelpSetURL(), (String) params.get("data"));
        } catch (Exception ex) {
            throw new Error("Trouble getting URL to TOC data; " + ex);
        }
        BufferedInputStream in;
        nodeStack = new Stack();
        nodeStack.push(topNode);
        tagStack = new Stack();
        itemStack = new Stack();
        defaultLocale = null;
        lastLocale = null;
        Reader src;
        try {
            URLConnection uc = url.openConnection();
            src = XmlReader.createReader(uc);
            Parser p = new Parser(src);
            currentParseHS = hs;
            p.addParserListener(this);
            p.parse();
            src.close();
        } catch (Exception e) {
            debug("exception thrown" + e.toString());
            e.printStackTrace();
            return;
        }
        tagStack = null;
        itemStack = null;
    }
