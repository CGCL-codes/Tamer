    @Test
    public void submaster2() {
        addCue();
        addCue();
        setSubmasterLevelValue(0, 0, 0.30f);
        setSubmasterLevelValue(1, 0, 0.60f);
        setSubmasterLevelValue(2, 0, 0.90f);
        lightCues.setCueSubmaster(0, 0, 0.5f);
        assertLevelValue("(1a)", 0.15f, getDetail(0).getChannelLevel(0).getValue());
        assertLevelValue("(1b)", 0.15f, getDetail(1).getChannelLevel(0).getValue());
        lightCues.setCueSubmaster(0, 1, 0.5f);
        assertLevelValue("(2a)", 0.30f, getDetail(0).getChannelLevel(0).getValue());
        assertLevelValue("(2b)", 0.30f, getDetail(1).getChannelLevel(0).getValue());
        lightCues.setCueSubmaster(0, 2, 1f);
        assertLevelValue("(3a)", 0.90f, getDetail(0).getChannelLevel(0).getValue());
        assertLevelValue("(3b)", 0.90f, getDetail(1).getChannelLevel(0).getValue());
    }
