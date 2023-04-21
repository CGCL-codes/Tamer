    @Test
    public void testActiveChangesOnly() {
        LightCueDetail cue = new LightCueDetail(t, 3, 3);
        cue.getChannelLevel(0).setChannelValue(1f);
        cue.getChannelLevel(1).setChannelValue(1f);
        cue.getChannelLevel(2).setChannelValue(1f);
        cue.getChannelLevel(1).getChannelLevelValue().setActive(false);
        cue.getChannelLevel(1).getSubmasterLevelValue().setActive(false);
        ChannelChange[] changes = cue.getChannelChanges().toArray();
        assertEquals(changes.length, 2);
        assertEquals(changes[0].getChannelId(), 1);
        assertEquals(changes[1].getChannelId(), 3);
    }
