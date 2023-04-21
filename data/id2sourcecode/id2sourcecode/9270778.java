    private void connectRecording() throws IOException {
        mixer.connectOutput(RECORDING_LEFT.getChannel(), recorder.input, JSynUtil.LEFT_CHANNEL);
        mixer.connectOutput(RECORDING_RIGHT.getChannel(), recorder.input, JSynUtil.RIGHT_CHANNEL);
        wfw.writeHeader(NUM_REC_CHANNELS, 44100);
    }
