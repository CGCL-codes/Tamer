    private boolean createURLPage(BookPage bookPage) {
        URL url = null;
        editorPane = new BookEditorPane();
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
                if (!(editorPane.getEditorKit() instanceof HTMLEditorKit) && !(editorPane.getEditorKit() instanceof RTFEditorKit)) {
                    isValid = false;
                } else isValid = true;
            }
        } catch (IOException e) {
        }
        return isValid;
    }
