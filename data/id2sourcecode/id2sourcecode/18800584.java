    @Test
    public void showChange() {
        Show show = ShowBuilder.example();
        getContext().setShow(show);
        Cues cues = CuesFactory.populateCues(getContext());
        cues.setCurrent(0);
        cues.getLightCues().setChannel(0, 0, 0.5f);
        assertEquals(lastLayerId, Lanbox.ENGINE_SHEET);
        assertEquals(lastChange.getChannelId(), 1);
        assertEquals(lastChange.getDmxValue(), 127);
    }
