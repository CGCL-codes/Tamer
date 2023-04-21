    public void testLexer() throws TokenStreamException, RecognitionException {
        Lang4jLexer lexer = new Lang4jLexer(new ANTLRStringStream("'X' \"X\" '{'"));
        org.antlr.runtime.Token token = lexer.nextToken();
        assertEquals(Lang4jLexer.CHAR_LITERAL, token.getType());
        token = lexer.nextToken();
        assertEquals(Lang4jLexer.WS, token.getType());
        assertEquals(Token.HIDDEN_CHANNEL, token.getChannel());
        token = lexer.nextToken();
        assertEquals(Lang4jLexer.STRING_LITERAL, token.getType());
        token = lexer.nextToken();
        assertEquals(Lang4jLexer.WS, token.getType());
        token = lexer.nextToken();
        assertEquals(Lang4jLexer.CHAR_LITERAL, token.getType());
    }
