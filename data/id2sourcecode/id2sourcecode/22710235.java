    public void testGetChannel() {
        assertEquals("Canale5", programTest.getChannel().getDisplayName());
        assertEquals("http://www.canale5.it", programTest.getChannel().getId().toString());
        assertFalse("Italia1".equals(programTest.getChannel().getDisplayName()));
    }
