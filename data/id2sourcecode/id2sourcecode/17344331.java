    @Test
    public void testUnrealJoinComplete() {
        JoinCompleteEvent je = (JoinCompleteEvent) events.get(1);
        assertTrue(je.getChannel().getName(), je.getChannel().getName().equals("#tvtorrents"));
    }
