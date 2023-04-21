    public ResultsPage(URL url) {
        try {
            Tidy tidy = new Tidy();
            tidy.setShowWarnings(false);
            tidy.setQuiet(true);
            NodeList body = tidy.parseDOM(url.openStream(), null).getElementsByTagName("body");
            NodeList nl = body.item(0).getChildNodes();
            PatternSequence ps = new PatternSequence();
            ps.findSequence(nl);
            Vector train = ps.getTrain();
            NodeList tNl = ps.getTrainNodeList();
            int begin = ((Integer) train.elementAt(0)).intValue();
            int end = ((Integer) train.elementAt(1)).intValue();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
