    private void validateValues(double[] expected) {
        EEGChannelValue value;
        for (int i = 0; i < getValues().size(); i++) {
            value = getValues().get(i);
            double val = roundValue(value.getChannelStrengthWithCalibration());
            assertTrue("Expected: " + expected[i] + ", actual: " + val + " for channel " + value.getChannelState().getFrequencyType().getDescription(), val == expected[i]);
        }
    }
