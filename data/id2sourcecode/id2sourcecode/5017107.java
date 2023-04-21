    @Test
    public void testEntities() throws IOException {
        Show show1 = ShowBuilder.build(2, 2, 4, "");
        Channel channel = show1.getChannels().get(0);
        channel.setName("Test & < > ; ' \"");
        Show show2 = doTest(show1);
        assertEquals(show1, show2);
    }
