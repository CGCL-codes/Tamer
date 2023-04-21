    private boolean createURLPage(boolean listenHyperLinks) {
        URL url = null;
        try {
            url = new URL(bookPage.getUri());
            url.openStream().close();
        } catch (Exception e) {
            isValid = false;
        }
        try {
            if (isValid) {
                editorPane.setPage(url);
                editorPane.setEditable(false);
                if (listenHyperLinks) editorPane.addHyperlinkListener(new BookHyperlinkListener());
                if (!(editorPane.getEditorKit() instanceof HTMLEditorKit) && !(editorPane.getEditorKit() instanceof RTFEditorKit)) {
                    isValid = false;
                }
            }
        } catch (IOException e) {
        }
        return isValid;
    }
