    @Test
    public void changeChannelName() {
        sortChannelTableOnChannelNr();
        JTableOperator table = tester.getTable("channelTable");
        table.clickForEdit(0, 1);
        table.changeCellObject(0, 1, "New name");
        Util.sleep(100);
        String name = tester.getContext().getShow().getChannels().get(0).getName();
        assertEquals(name, "New name");
    }
