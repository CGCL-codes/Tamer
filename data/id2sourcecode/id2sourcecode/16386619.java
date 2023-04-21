    public static void html2xhtml(Reader reader, Writer writer) throws IOException, BadLocationException {
        HTMLEditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        kit.read(reader, doc, doc.getLength());
        XHTMLWriter xhw = new XHTMLWriter(writer, (HTMLDocument) doc);
        xhw.write();
    }
