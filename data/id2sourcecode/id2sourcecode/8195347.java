    @Test
    public void equals() {
        LightCueDetail cue1 = new LightCueDetail(t, 10, 20);
        LightCueDetail cue2 = new LightCueDetail(t, 10, 20);
        assertEquals(cue1, cue2);
        cue1.getChannelLevel(0).setDerived(false);
        assertFalse(cue1.equals(cue2));
        cue2.getChannelLevel(0).setDerived(false);
        assertTrue(cue1.equals(cue2));
        cue1.getSubmasterLevel(0).setDerived(false);
        assertFalse(cue1.equals(cue2));
        cue2.getSubmasterLevel(0).setDerived(false);
        assertTrue(cue1.equals(cue2));
        cue1.getTiming().setFadeInTime(Time.TIME_1S);
        assertFalse(cue1.equals(cue2));
        cue2.getTiming().setFadeInTime(Time.TIME_1S);
        assertTrue(cue1.equals(cue2));
    }
