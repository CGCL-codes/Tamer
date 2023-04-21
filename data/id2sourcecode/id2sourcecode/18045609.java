    public DocumentSummary parseDocument(URL url) throws IOException, DocumentHandlerException {
        RTFEditorKit kit = new RTFEditorKit();
        Document doc = kit.createDefaultDocument();
        try {
            kit.read(url.openStream(), doc, 0);
            String plainText = doc.getText(0, doc.getLength());
            DocumentSummary docSummary = new DocumentSummary();
            docSummary.contentReader = new StringReader(plainText);
            return docSummary;
        } catch (IOException e) {
            throw new DocumentHandlerException("Error reading RTF file", e);
        } catch (BadLocationException e) {
            throw new DocumentHandlerException("Internal error reading RTF file", e);
        }
    }
