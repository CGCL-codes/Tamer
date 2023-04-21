    private void doTestTiming(final Time time, final int waitTime) {
        execute(new LayerSetFading(1, false));
        setChannel1(0);
        execute(new LayerSetFading(1, true));
        execute(new LayerSetFadeType(1, FadeType.CROSS_FADE));
        execute(new LayerSetFadeTime(1, time));
        execute(new LayerResume(1));
        Chrono chrono = new Chrono();
        setChannel1(255);
        chrono.waitFor(waitTime);
        execute(new LayerPause(1));
        LayerStatus status = new LayerStatus();
        execute(new LayerGetStatus(1, status));
        int millis = status.getFadeTimeRemaining();
        int value = getChannel1();
        int expectedValue = 255 * (time.getMillis() - millis) / time.getMillis();
        assertEquals(value, expectedValue, 1);
    }
