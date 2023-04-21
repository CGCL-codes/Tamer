    @Test
    public void testId() throws Exception {
        assertNotNull("Make sure there is an id.", client.nextId());
        assertEquals("Make sure there is an address", client.getChannel().getLocalAddress(), client.nextId().getSource());
    }
