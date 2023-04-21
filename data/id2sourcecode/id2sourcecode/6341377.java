    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, method = "getMinBufferSize", args = { int.class, int.class, int.class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getAudioFormat", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getChannelConfiguration", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getSampleRate", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getStreamType", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getChannelCount", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getNotificationMarkerPosition", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "setNotificationMarkerPosition", args = { int.class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "setPositionNotificationPeriod", args = { int.class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getPositionNotificationPeriod", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "setState", args = { int.class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getNativeFrameCount", args = {  }) })
    public void testAudioTrackProperties() throws Exception {
        final String TEST_NAME = "testAudioTrackProperties";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        MockAudioTrack track = new MockAudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT, 2 * minBuffSize, TEST_MODE);
        assertEquals(TEST_NAME, TEST_FORMAT, track.getAudioFormat());
        assertEquals(TEST_NAME, TEST_CONF, track.getChannelConfiguration());
        assertEquals(TEST_NAME, TEST_SR, track.getSampleRate());
        assertEquals(TEST_NAME, TEST_STREAM_TYPE, track.getStreamType());
        final int hannelCount = 1;
        assertEquals(hannelCount, track.getChannelCount());
        final int notificationMarkerPosition = 0;
        assertEquals(TEST_NAME, notificationMarkerPosition, track.getNotificationMarkerPosition());
        final int markerInFrames = 2;
        assertEquals(TEST_NAME, AudioTrack.SUCCESS, track.setNotificationMarkerPosition(markerInFrames));
        assertEquals(TEST_NAME, markerInFrames, track.getNotificationMarkerPosition());
        final int positionNotificationPeriod = 0;
        assertEquals(TEST_NAME, positionNotificationPeriod, track.getPositionNotificationPeriod());
        final int periodInFrames = 2;
        assertEquals(TEST_NAME, AudioTrack.SUCCESS, track.setPositionNotificationPeriod(periodInFrames));
        assertEquals(TEST_NAME, periodInFrames, track.getPositionNotificationPeriod());
        assertEquals(TEST_NAME, AudioTrack.STATE_INITIALIZED, track.getState());
        track.setState(AudioTrack.STATE_NO_STATIC_DATA);
        assertEquals(TEST_NAME, AudioTrack.STATE_NO_STATIC_DATA, track.getState());
        track.setState(AudioTrack.STATE_UNINITIALIZED);
        assertEquals(TEST_NAME, AudioTrack.STATE_UNINITIALIZED, track.getState());
        final int nativeFrameCount = 2400;
        assertTrue(TEST_NAME, track.getNativeFrameCount() >= nativeFrameCount);
    }
