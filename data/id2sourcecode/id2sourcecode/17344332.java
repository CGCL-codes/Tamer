    @Test
    public void testSnircdJoinComplete() {
        JoinCompleteEvent je = (JoinCompleteEvent) events.get(2);
        assertTrue(je.getChannel().getName(), je.getChannel().getName().equals("#cod4.wars"));
    }
