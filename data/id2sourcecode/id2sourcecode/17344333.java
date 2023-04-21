    @Test
    public void testHyperionJoinComplete() {
        JoinCompleteEvent je = (JoinCompleteEvent) events.get(3);
        assertTrue(je.getChannel().getName(), je.getChannel().getName().equals("#ubuntu"));
    }
