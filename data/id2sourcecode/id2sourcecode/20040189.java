    public static void assertToken(int expectedChannel, int expectedType, String expectedText, Token token) {
        assertEquals(expectedChannel, token.getChannel());
        assertEquals("failed to match token types,", expectedType, token.getType());
        assertEquals("failed to match token text,", expectedText, token.getText());
    }
