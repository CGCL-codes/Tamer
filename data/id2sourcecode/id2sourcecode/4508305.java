    public void testDigest_OneTokenOperator() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(TokenFactory.getInstance().newToken("{-opt|x|y}"));
        assertEquals(tokens, URITemplate.digest("{-opt|x|y}"));
    }
