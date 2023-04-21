    public void testDigest_OneTokenInTheMiddle() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(new TokenLiteral("http://acme.com/"));
        tokens.add(new TokenVariable("x"));
        tokens.add(new TokenLiteral("/text"));
        assertEquals(tokens, URITemplate.digest("http://acme.com/{x}/text"));
    }
