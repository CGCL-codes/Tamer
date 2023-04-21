    @Test
    public void testCopyGroups1() {
        oldShow = ShowBuilder.build(2, 3, 2, "");
        newShow = ShowBuilder.build(dirty, 4, 3, 4, "");
        Channel channel1 = oldShow.getChannels().get(0);
        Channel channel2 = oldShow.getChannels().get(1);
        Group group1 = new Group("Group 1");
        Group group2 = new Group("Group 2");
        group1.add(channel1);
        group1.add(channel2);
        group2.add(channel2);
        oldShow.getGroups().add(group1);
        oldShow.getGroups().add(group2);
        copy();
        Group newGroup1 = newShow.getGroups().get(0);
        Group newGroup2 = newShow.getGroups().get(1);
        assertEquals(newGroup1.getName(), "Group 1");
        assertEquals(newGroup2.getName(), "Group 2");
        assertEquals(newGroup1.size(), 2);
        assertEquals(newGroup2.size(), 1);
        assertEquals(newGroup1.get(0).getName(), "Channel 1");
        assertEquals(newGroup1.get(1).getName(), "Channel 2");
        assertEquals(newGroup2.get(0).getName(), "Channel 2");
        dirty.clear();
        newGroup1.setName("new");
        assertTrue(dirty.isDirty());
    }
