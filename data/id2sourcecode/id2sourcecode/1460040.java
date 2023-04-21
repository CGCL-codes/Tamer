    public static Dialog ParseFile(InputStream is) {
        parser = new CSSParser();
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);
        Document doc;
        try {
            doc = builder.build(is);
            Element root = doc.getRootElement();
            if (root == null) return null;
            Dialog dialog = new Dialog();
            ParseHeader(root, dialog);
            ParseChannels(root, dialog);
            ParseTracks(root, dialog);
            dialog.build();
            if (dialog.getChannels().isEmpty()) dialog = null;
            return dialog;
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
