    @Test
    public void pauseAndResumeFade() {
        execute(new LayerSetFading(1, false));
        setChannel1(0);
        execute(new LayerResume(1));
        execute(new LayerSetFading(1, true));
        execute(new LayerSetFadeType(1, FadeType.CROSS_FADE));
        execute(new LayerSetFadeTime(1, Time.TIME_2S));
        Chrono chrono = new Chrono();
        setChannel1(255);
        chrono.waitFor(500);
        execute(new LayerPause(1));
        assertEquals(getChannel1(), 255 / 4, 10);
        sleep(1000);
        chrono = new Chrono();
        execute(new LayerResume(1));
        chrono.waitFor(500);
        assertEquals(getChannel1(), 255 / 2, 10);
        chrono.waitFor(1000);
        assertEquals(getChannel1(), 3 * 255 / 4, 10);
        chrono.waitFor(1500);
        assertEquals(getChannel1(), 255, 10);
        sleep(50);
        LayerStatus status = new LayerStatus();
        execute(new LayerGetStatus(1, status));
        assertFalse(status.isPausing());
        assertEquals(status.getFadeTimeRemaining(), 0);
    }
