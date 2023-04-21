    public void openFileFromStream(java.io.File p_file, boolean openAsText) {
        Document doc = null;
        EditorKit kit = null;
        boolean extentionOkay = false;
        String extension = "txt";
        if ((p_file != null) && p_file.isFile()) {
            try {
                java.net.URL url = p_file.toURI().toURL();
                if (!openAsText) extension = net.hussnain.io.Utilities.parseExtension(url.getPath());
                if (extension.equals("rtf")) {
                    kit = new RTFEditorKit();
                    doc = new RabtStyledDocument(styleContext);
                    extentionOkay = true;
                } else {
                    if (extension.equals("txt")) {
                        kit = new StyledEditorKit();
                        doc = new RabtStyledDocument(styleContext);
                        extentionOkay = true;
                    } else {
                        if (extension.equals("htm") || extension.equals("html")) {
                            doc = new RabtHTMLDocument(styleSheet);
                            kit = new HTMLEditorKit();
                            extentionOkay = true;
                            ((HTMLDocument) doc).setParser(getParserHTML());
                        }
                    }
                }
                if (extentionOkay) {
                    InputStream input = url.openStream();
                    textPane.setEditorKit(kit);
                    kit.read(input, doc, 0);
                    textPane.setDocument(doc);
                    input.close();
                } else textPane.setPage(url);
                textPane.getDocument().addUndoableEditListener(this);
            } catch (java.io.IOException exp) {
                logger.warning("couldnt open file at " + p_file.toString());
            } catch (javax.swing.text.BadLocationException exp) {
                logger.warning("couldnt open file at " + p_file.toString());
            }
        }
    }
