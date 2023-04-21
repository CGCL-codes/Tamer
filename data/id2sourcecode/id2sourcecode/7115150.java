    private static void printTokens(URI uri) {
        Token token;
        OmttLexer lexer = getLexer(uri);
        while ((token = lexer.nextToken()) != Token.EOF_TOKEN) {
            String txt = "Token" + "(" + (lexer.brackets.empty() ? "-" : lexer.brackets.peek()) + "): " + OmttParser.tokenNames[token.getType()] + ": " + token.getText();
            if (token.getChannel() != OmttParser.HIDDEN) System.out.println(txt);
        }
    }
