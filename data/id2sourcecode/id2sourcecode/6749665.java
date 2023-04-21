    @Test
    public void patch() {
        tester.getTable("channelTable").selectCell(1, 0);
        tester.getTable("detailTable").selectCell(0, 2);
        tester.pushButtonNamed("patch.action.patch");
        Dimmer dimmer = tester.getContext().getShow().getDimmers().get(0);
        assertEquals(dimmer.getChannelId(), 1);
    }
