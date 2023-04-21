    public void testDigest_OneTokenVariable() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(new TokenVariable("x"));
        assertEquals(tokens, URITemplate.digest("{x}"));
    }
