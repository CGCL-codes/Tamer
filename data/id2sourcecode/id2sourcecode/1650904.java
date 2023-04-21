    public static void process(Reader reader, Writer writer, IHTMLFilter htmlFilter, boolean convertIntoValidXML) throws HandlingException {
        try {
            ANTLRStringStream input = new ANTLRReaderStream(reader);
            htmlLexerLexer lex = new htmlLexerLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lex);
            htmlParserParser parser = new htmlParserParser(tokens);
            htmlParserParser.document_return root = parser.document();
            CommonTreeNodeStream nodes = new CommonTreeNodeStream((Tree) root.getTree());
            htmlTreeParser walker = new htmlTreeParser(nodes);
            topNode = new ThreadLocal();
            currentNode = new ThreadLocal();
            attrNode = new ThreadLocal();
            walker.document();
            TagNode top = (TagNode) topNode.get();
            top.writeAll(writer, htmlFilter, convertIntoValidXML, false);
        } catch (IOException ioe) {
            throw new HandlingException("Could not parse document");
        } catch (RecognitionException re) {
            throw new HandlingException("Could not parse document");
        }
    }
