    @Test
    public void testSnircdJoinComplete() {
        JoinEvent je = (JoinEvent) events.get(25);
        assertTrue(je.getChannel().getName(), je.getChannel().getName().equals("#cod4.wars"));
        assertTrue(je.getNick().equals("kalleKula"));
        assertTrue(je.getHostName().equals("90-227-48-98-no88.tbcn.telia.com"));
        assertTrue(je.getUserName(), je.getUserName().equals("~fa"));
    }
