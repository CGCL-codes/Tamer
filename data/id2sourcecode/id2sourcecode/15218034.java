    private static void parseOneFile(Reader reader, PrintWriter writer, String baseName, String filename) {
        N3ParserEventHandler handler = null;
        handler = null;
        if (printN3 || debug) {
            N3EventPrinter p = new N3EventPrinter(writer);
            if (verbose) p.printStartFinish = true;
            handler = p;
        } else handler = new N3ErrorPrinter(writer);
        try {
            N3Parser n3Parser = new N3Parser(reader, handler);
            n3Parser.parse();
        } catch (antlr.RecognitionException ex) {
            System.exit(9);
        } catch (antlr.TokenStreamException tokEx) {
            System.exit(9);
        }
    }
