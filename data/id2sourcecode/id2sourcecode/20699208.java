    @Test
    public void getChannelIndexOutOfBoundsException() {
        Channel channel1 = new Channel(0, "1");
        Channel channel2 = new Channel(1, "2");
        Group group1 = new Group("");
        Group group2 = new Group("");
        group1.add(channel1);
        group2.add(channel2);
        Groups groups = new Groups();
        groups.add(group1);
        groups.add(group2);
        group1.setEnabled(true);
        group2.setEnabled(true);
        try {
            groups.getChannel(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 2, Size: 2", e.getMessage());
        }
        group1.setEnabled(false);
        group2.setEnabled(true);
        try {
            groups.getChannel(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 1, Size: 1", e.getMessage());
        }
        group1.setEnabled(false);
        group2.setEnabled(false);
        try {
            groups.getChannel(0);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 0, Size: 0", e.getMessage());
        }
    }
