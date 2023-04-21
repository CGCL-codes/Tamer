    @Test
    public void testHyperionJoinComplete() {
        JoinEvent je = (JoinEvent) events.get(23);
        assertTrue(je.getChannel().getName(), je.getChannel().getName().equals("#ubuntu"));
        assertTrue(je.getNick().equals("markl_"));
        assertTrue(je.getHostName().equals("c-24-10-221-6.hsd1.co.comcast.net"));
        assertTrue(je.getUserName(), je.getUserName().equals("n=mark"));
    }
