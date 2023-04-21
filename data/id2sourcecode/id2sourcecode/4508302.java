    public void testDigest_EmptyString() {
        List<Token> tokens = new ArrayList<Token>();
        assertEquals(tokens, URITemplate.digest(""));
    }
