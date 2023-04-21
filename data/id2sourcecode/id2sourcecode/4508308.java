    public void testDigest_TwoTokens() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(new TokenLiteral("http://acme.com/"));
        tokens.add(new TokenVariable("x"));
        tokens.add(new TokenLiteral("/"));
        tokens.add(new TokenVariable("y"));
        assertEquals(tokens, URITemplate.digest("http://acme.com/{x}/{y}"));
    }
