    private boolean createResourcePage(BookPage bookPage, boolean export) {
        editorPane = new BookEditorPaneEditor(currentBookPage);
        ((BookEditorPaneEditor) editorPane).setExport(export);
        URL url = AssetsController.getResourceAsURLFromZip(bookPage.getUri());
        String ext = url.getFile().substring(url.getFile().lastIndexOf('.') + 1, url.getFile().length()).toLowerCase();
        if (ext.equals("html") || ext.equals("htm") || ext.equals("rtf")) {
            StringBuffer textBuffer = new StringBuffer();
            InputStream is = null;
            try {
                is = url.openStream();
                int c;
                while ((c = is.read()) != -1) {
                    textBuffer.append((char) c);
                }
            } catch (IOException e) {
                isValid = false;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        isValid = false;
                    }
                }
            }
            if (ext.equals("html") || ext.equals("htm")) {
                editorPane.setContentType("text/html");
                editorPane.setText(textBuffer.toString());
                try {
                    editorPane.setDocumentBase(new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                editorPane.setContentType("text/rtf");
                editorPane.setText(textBuffer.toString());
            }
            isValid = true;
        }
        return isValid;
    }
