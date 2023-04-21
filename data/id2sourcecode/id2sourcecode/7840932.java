    public double getChannelStrengthWithCalibration() {
        double cs = channelStrengthWithCalibration == 0 ? channelStrength : channelStrengthWithCalibration;
        if (getNormalizedFactor() > 0) {
            return new BigDecimal(cs).divide(new BigDecimal(getNormalizedFactor()), new MathContext(5, RoundingMode.HALF_UP)).doubleValue();
        }
        return cs;
    }
