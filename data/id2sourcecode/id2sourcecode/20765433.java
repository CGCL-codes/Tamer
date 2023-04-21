    public void testWriteArrayString() {
        Log4jMessageListener.loadDefaultSettings();
        String[] expected = { "blah", "help-12,", "car\"dbo*ard\"" };
        assertEquals("\"blah\", \"help-12,\", \"car\\\"dbo*ard\\\"\"", ArrayValueHandler.writeArray(expected, ','));
        assertEqualsArray(expected, ArrayValueHandler.readArray(String[].class.getName(), ArrayValueHandler.writeArray(expected, ','), ','));
    }
