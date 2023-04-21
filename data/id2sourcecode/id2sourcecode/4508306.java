    public void testDigest_TwoToken() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(new TokenLiteral("http://acme.com/"));
        tokens.add(new TokenVariable("x"));
        assertEquals(tokens, URITemplate.digest("http://acme.com/{x}"));
    }
