    public void testGettersSetters() {
        ChannelDecisionManagerImpl cdm = new ChannelDecisionManagerImpl();
        assertNull(cdm.getChannelProcessors());
        MockChannelProcessor cpXyz = new MockChannelProcessor("xyz", false);
        MockChannelProcessor cpAbc = new MockChannelProcessor("abc", false);
        List list = new Vector();
        list.add(cpXyz);
        list.add(cpAbc);
        cdm.setChannelProcessors(list);
        assertEquals(list, cdm.getChannelProcessors());
    }
