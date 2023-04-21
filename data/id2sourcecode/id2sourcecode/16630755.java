    @Test
    public void testBahamutJoin() {
        JoinEvent je = (JoinEvent) events.get(6);
        assertTrue(je.getChannel().getName(), je.getChannel().getName().equals("#perkosa"));
        assertTrue(je.getNick().equals("David`KereN"));
        assertTrue(je.getHostName().equals("we.wish.you.a.merry.christmas.pp.ru"));
        assertTrue(je.getUserName(), je.getUserName().equals("real"));
    }
