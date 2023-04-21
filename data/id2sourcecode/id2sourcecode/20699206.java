    @Test
    public void getChannelsNotInGroup() {
        Group group1 = new Group();
        Group group2 = new Group();
        Groups groups = new Groups();
        groups.add(group1);
        groups.add(group2);
        int indexes1[] = { 0, 1, 2, 3, 4 };
        assertArrayEquals(indexes1, groups.getChannelsNotInGroup(true, 0, 5));
        group2.add(new Channel(2, ""));
        int indexes2[] = { 0, 1, 3, 4 };
        assertArrayEquals(indexes1, groups.getChannelsNotInGroup(false, 0, 5));
        assertArrayEquals(indexes2, groups.getChannelsNotInGroup(true, 0, 5));
    }
