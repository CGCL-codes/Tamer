    @Test
    public void pauseFadeAndStop() {
        execute(new LayerSetFading(1, false));
        setChannel1(0);
        execute(new LayerSetFading(1, true));
        execute(new LayerSetFadeType(1, FadeType.CROSS_FADE));
        execute(new LayerSetFadeTime(1, Time.TIME_2S));
        execute(new LayerResume(1));
        Chrono chrono = new Chrono();
        setChannel1(255);
        chrono.waitFor(500);
        execute(new LayerPause(1));
        LayerStatus status = new LayerStatus();
        execute(new LayerGetStatus(1, status));
        assertTrue(status.isPausing());
        assertEquals(status.getFadeTimeRemaining(), 1500, 100);
        assertEquals(getChannel1(), 255 / 4, 10);
        chrono.waitFor(1000);
        assertEquals(getChannel1(), 255 / 4, 10);
        execute(new LayerSetFadeType(1, FadeType.OFF));
        setChannel1(63);
        execute(new LayerResume(1));
        execute(new LayerGetStatus(1, status));
        assertFalse(status.isPausing());
        chrono.waitFor(1500);
        assertEquals(getChannel1(), 63);
    }
