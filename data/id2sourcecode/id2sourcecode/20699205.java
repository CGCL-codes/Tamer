    @Test
    public void getChannelCount() {
        Group group1 = new Group();
        Group group2 = new Group();
        Groups groups = new Groups();
        groups.add(group1);
        groups.add(group2);
        assertEquals(0, groups.getChannelCount(5));
        group1.add(new Channel(2, ""));
        assertEquals(1, groups.getChannelCount(5));
        group2.add(new Channel(3, ""));
        assertEquals(2, groups.getChannelCount(5));
    }
