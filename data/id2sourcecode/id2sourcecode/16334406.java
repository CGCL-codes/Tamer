        ConversionContext(Reader reader, Writer writer, String encoding) {
            this.tokenizer = new DefaultHTMLTokenizer(reader);
            this.path = new Stack<String>();
            this.xmlHeaderCreated = false;
            this.rootCreated = false;
            this.writer = writer;
            this.encoding = encoding;
        }
