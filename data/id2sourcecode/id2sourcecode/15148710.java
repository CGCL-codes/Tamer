    @Test
    public void sortChannelName() {
        patch.getDetail(0).getDimmer().setChannel(null);
        patch.getDetail(10).getDimmer().getChannel().setName("A");
        patch.getDetail(11).getDimmer().getChannel().setName("A");
        patch.setSortColumn(PatchDetail.CHANNEL_NAME);
        assertEquals(patch.getDetail(0).getDimmer().getId(), 10);
        assertEquals(patch.getDetail(1).getDimmer().getId(), 11);
        assertEquals(patch.getDetail(23).getDimmer().getId(), 0);
        assertEquals(patch.getDetail(24).getDimmer().getId(), 24);
    }
