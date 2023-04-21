    @Test
    public void testChannelListEvent() {
        ChannelListEvent cle = IRCEventFactory.chanList(":anthony.freenode.net 322 mohadib_ #jerklib 5 :JerkLib IRC Library - https://sourceforge.net/projects/jerklib", connection);
        assertNotNull(cle);
        assertEquals("#jerklib", cle.getChannelName());
        assertEquals(5, cle.getNumberOfUser());
        assertEquals("JerkLib IRC Library - https://sourceforge.net/projects/jerklib", cle.getTopic());
    }
