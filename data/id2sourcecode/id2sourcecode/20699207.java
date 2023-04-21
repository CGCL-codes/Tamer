    @Test
    public void getChannel() {
        Channel channel1 = new Channel(0, "1");
        Channel channel2 = new Channel(1, "2");
        Channel channel3 = new Channel(2, "3");
        Channel channel4 = new Channel(3, "4");
        Group group1 = new Group("");
        Group group2 = new Group("");
        group1.add(channel1);
        group1.add(channel2);
        group2.add(channel3);
        group2.add(channel4);
        Groups groups = new Groups();
        groups.add(group1);
        groups.add(group2);
        group1.setEnabled(true);
        group2.setEnabled(true);
        assertEquals(channel1, groups.getChannel(0));
        assertEquals(channel2, groups.getChannel(1));
        assertEquals(channel3, groups.getChannel(2));
        assertEquals(channel4, groups.getChannel(3));
        group1.setEnabled(false);
        group2.setEnabled(true);
        assertEquals(channel3, groups.getChannel(0));
        assertEquals(channel4, groups.getChannel(1));
    }
