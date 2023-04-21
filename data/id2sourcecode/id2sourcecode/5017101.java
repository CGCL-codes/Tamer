    @Test
    public void writeAndReadGroups() throws IOException {
        Show show1 = ShowBuilder.example();
        Group group1 = new Group("Group one");
        Group group2 = new Group("Group two");
        Group group3 = new Group("Group three");
        show1.getGroups().add(group1);
        show1.getGroups().add(group2);
        show1.getGroups().add(group3);
        group1.add(show1.getChannels().get(0));
        group1.add(show1.getChannels().get(1));
        group1.add(show1.getChannels().get(2));
        group2.add(show1.getChannels().get(10));
        group2.add(show1.getChannels().get(5));
        group2.add(show1.getChannels().get(1));
        Show show2 = doTest(show1);
        Groups groups = show2.getGroups();
        assertEquals(groups.size(), 3);
        assertEquals(groups.get(0).getName(), "Group one");
        assertEquals(groups.get(1).getName(), "Group two");
        assertEquals(groups.get(2).getName(), "Group three");
        assertEquals(groups.get(0).size(), 3);
        assertEquals(groups.get(1).size(), 3);
        assertEquals(groups.get(2).size(), 0);
        Channel[] channels = groups.get(0).getChannels();
        assertEquals(channels[0].getId(), 0);
        assertEquals(channels[1].getId(), 1);
        assertEquals(channels[2].getId(), 2);
        channels = groups.get(1).getChannels();
        assertEquals(channels[0].getId(), 10);
        assertEquals(channels[1].getId(), 5);
        assertEquals(channels[2].getId(), 1);
        channels = groups.get(2).getChannels();
        assertEquals(channels.length, 0);
    }
