    @Test
    public void unPatch() {
        defaultPatch();
        JTableOperator detailTable = tester.getTable("detailTable");
        detailTable.clickOnCell(0, PatchDetail.CHANNEL_NAME);
        Dimmer dimmer = tester.getContext().getShow().getDimmers().get(0);
        assertNotNull(dimmer.getChannel());
        tester.pushButtonNamed("patch.action.unpatch");
        assertNull(dimmer.getChannel());
    }
