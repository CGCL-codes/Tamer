    @Test
    public void testContainsNick() {
        Channel chan = session.getChannel("#ubuntu");
        assertTrue(!chan.getNicks().contains("unstable"));
        assertTrue(chan.getNicks().contains("rosco"));
    }
