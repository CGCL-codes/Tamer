    @Test
    public void writeAndReadDimmers() throws IOException {
        Show show1 = ShowBuilder.build(2, 2, 4, "");
        Dimmer dimmer1 = show1.getDimmers().get(0);
        Dimmer dimmer2 = show1.getDimmers().get(1);
        Dimmer dimmer3 = show1.getDimmers().get(2);
        Dimmer dimmer4 = show1.getDimmers().get(3);
        Channel channel1 = show1.getChannels().get(0);
        Channel channel2 = show1.getChannels().get(1);
        dimmer1.setName("D1");
        dimmer1.setName("D2");
        dimmer1.setName(" D 3 ");
        dimmer1.setName("");
        dimmer1.setChannel(channel2);
        dimmer2.setChannel(null);
        dimmer3.setChannel(channel1);
        dimmer4.setChannel(channel1);
        Show show2 = doTest(show1);
        assertEquals(show1, show2);
    }
