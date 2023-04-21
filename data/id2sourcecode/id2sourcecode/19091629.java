    @Test
    public void submaster() {
        addCue();
        addCue();
        setSubmasterLevelValue(0, 0, 0.8f);
        lightCues.setCueSubmaster(0, 0, 0.5f);
        CueChannelLevel level = getDetail(0).getChannelLevel(0);
        assertLevelValue("value 0", 0.4f, level.getValue());
        assertLevelValue("channel value 0", 0f, level.getChannelLevelValue().getValue());
        assertLevelValue("submaster value 0", 0.4f, level.getSubmasterValue());
    }
