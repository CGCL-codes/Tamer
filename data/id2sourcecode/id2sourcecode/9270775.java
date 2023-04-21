    private void setGain(int gain, int idx, double gainValue) {
        double leftGain = gain == IS_BOTH || gain == IS_LEFT ? gainValue : 0;
        double rightGain = gain == IS_BOTH || gain == IS_RIGHT ? gainValue : 0;
        mixer.setGain(idx, LINEOUT_LEFT.getChannel(), leftGain);
        mixer.setGain(idx, LINEOUT_RIGHT.getChannel(), rightGain);
        mixer.setGain(idx, RECORDING_LEFT.getChannel(), leftGain);
        mixer.setGain(idx, RECORDING_RIGHT.getChannel(), rightGain);
    }
