    public void testDigest_OneTokenLiteral() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(new TokenLiteral("http://acme.com/"));
        assertEquals(tokens, URITemplate.digest("http://acme.com/"));
    }
