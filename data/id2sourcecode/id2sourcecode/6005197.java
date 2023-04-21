    public void changeClass(ID classID) {
        DefaultMutableTreeNode classTopNode = new DefaultMutableTreeNode();
        BufferedInputStream in;
        HelpModel helpModel = treenav.getModel();
        nodeStack = new Stack();
        nodeStack.push(classTopNode);
        itemStack = new Stack();
        tagStack = new Stack();
        defaultLocale = null;
        lastLocale = null;
        Reader src;
        try {
            HelpSet hs = helpModel.getHelpSet();
            URL url = hs.getCombinedMap().getURLFromID(classID);
            URLConnection uc = url.openConnection();
            src = XmlReader.createReader(uc);
            Parser p = new Parser(src);
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
        classtree.setModel(new DefaultTreeModel(classTopNode));
        classtree.expandRow(0);
        classtree.setSelectionRow(1);
    }
