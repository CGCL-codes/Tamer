    public WebScrollPane() {
        WebTextPane = new JTextPane() {

            protected InputStream getStream(URL url) throws IOException {
                return url.openStream();
            }
        };
        histStop = 0;
        histCurr = -1;
        historyVect = new Vector(1);
        WebTextPane.setToolTipText("");
        WebTextPane.setEditorKit(new HTMLEditorKit());
        WebTextPane.setEditable(false);
        WebTextPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent hyperlinkevent) {
                WebTextPane_hyperlinkUpdate(hyperlinkevent);
            }
        });
        getViewport().add(WebTextPane, null);
    }
