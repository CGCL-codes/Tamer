    @Test
    public void testBahamutJoinComplete() {
        JoinCompleteEvent je = (JoinCompleteEvent) events.get(0);
        assertTrue(je.getChannel().getName(), je.getChannel().getName().equals("#perkosa"));
    }
