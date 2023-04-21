    protected void format(Reader reader, Writer writer, final boolean formatDocInfo) throws WikiParserException {
        IWikiUri docUri = null;
        IWikiWriter wikiWriter = new WikiWriter(writer);
        IWikiInlineListener inlineListener = new WikiInlineHtmlFormatter(wikiWriter) {

            public void onReference(IWikiUri uri) {
                HtmlRenderer.this.formatLink(fWriter, uri);
            }
        };
        IWikiDocumentListener documentListener = new WikiDocumentHtmlFormatter(wikiWriter) {

            public void beginDocument(IWikiUri uri) {
                if (formatDocInfo) super.beginDocument(uri);
            }

            public void beginSection(IWikiUri uri) {
                if (formatDocInfo) super.beginSection(uri);
            }

            public void endDocument(IWikiUri uri) {
                if (formatDocInfo) super.endDocument(uri);
            }

            public void endSection(IWikiUri uri) {
                if (formatDocInfo) super.endDocument(uri);
            }

            protected void formatLink(IWikiUri uri) {
                HtmlRenderer.this.formatLink(fWriter, uri);
            }
        };
        IWikiParser parser = new WikiParser();
        parser.parse(reader, docUri, inlineListener, documentListener);
    }
