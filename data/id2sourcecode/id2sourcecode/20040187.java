    public static void assertToken(String message, int expectedChannel, int expectedType, String expectedText, Token token) {
        assertEquals(message + " (channel check)", expectedChannel, token.getChannel());
        assertEquals(message + " (type check)", expectedType, token.getType());
        assertEquals(message + " (text check)", expectedText, token.getText());
    }
