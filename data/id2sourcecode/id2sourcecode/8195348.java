    @Test
    public void copy() {
        LightCueDetail detail1 = new LightCueDetail(t, 2, 4);
        detail1.getChannelLevel(0).setChannelValue(0.4f);
        detail1.getChannelLevel(0).setSubmasterValue(0.5f);
        detail1.getSubmasterLevel(0).getLevelValue().setValue(0.6f);
        LightCueDetail detail2 = detail1.copy();
        assertEquals(detail1.getTiming(), detail2.getTiming());
        assertEquals(detail2.getChannelLevel(0).getChannelLevelValue().getValue(), 0.4f, 0.05f);
        assertEquals(detail2.getChannelLevel(0).getSubmasterValue(), 0.5f, 0.05f);
        assertEquals(detail2.getSubmasterLevel(0).getValue(), 0.6f, 0.05f);
        detail1.getTiming().setFadeInDelay(Time.TIME_2S);
        assertEquals(detail2.getTiming().getFadeInDelay(), Time.TIME_0S);
        detail1.getChannelLevel(0).setChannelValue(0.2f);
        assertEquals(detail2.getChannelLevel(0).getChannelLevelValue().getValue(), 0.4f, 0.05f);
        detail1.getChannelLevel(0).setSubmasterValue(0.3f);
        assertEquals(detail2.getChannelLevel(0).getSubmasterValue(), 0.5f, 0.05f);
        detail1.getSubmasterLevel(0).getLevelValue().setValue(0.4f);
        assertEquals(detail2.getSubmasterLevel(0).getValue(), 0.6f, 0.05f);
    }
