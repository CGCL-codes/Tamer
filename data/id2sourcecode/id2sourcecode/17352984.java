    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, method = "AudioRecord", args = { int.class, int.class, int.class, int.class, int.class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getAudioFormat", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getAudioSource", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getState", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getSampleRate", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getRecordingState", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getMinBufferSize", args = { int.class, int.class, int.class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getChannelCount", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "getChannelConfiguration", args = {  }) })
    public void testAudioRecordProperties() throws Exception {
        assertEquals(AudioFormat.ENCODING_PCM_16BIT, mAudioRecord.getAudioFormat());
        assertEquals(MediaRecorder.AudioSource.DEFAULT, mAudioRecord.getAudioSource());
        assertEquals(1, mAudioRecord.getChannelCount());
        assertEquals(AudioFormat.CHANNEL_CONFIGURATION_MONO, mAudioRecord.getChannelConfiguration());
        assertEquals(AudioRecord.STATE_INITIALIZED, mAudioRecord.getState());
        assertEquals(mHz, mAudioRecord.getSampleRate());
        assertEquals(AudioRecord.RECORDSTATE_STOPPED, mAudioRecord.getRecordingState());
        int bufferSize = AudioRecord.getMinBufferSize(mHz, AudioFormat.CHANNEL_CONFIGURATION_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
        assertTrue(bufferSize > 0);
    }
