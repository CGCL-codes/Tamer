    private void applyCalibration(List<EEGChannelValue> values) {
        List<EEGChannelState> states = getChannelStates();
        for (EEGChannelState state : states) {
            setCalibrationForState(state, values);
        }
    }
