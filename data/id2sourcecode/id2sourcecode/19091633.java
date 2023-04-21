    private void assertChannelValue(final int cueIndex, final int channelIndex, final float expected) {
        float value = getDetail(cueIndex).getChannelLevel(channelIndex).getValue();
        StringBuilder b = new StringBuilder();
        b.append("(channelLevel.getValue() at cueIndex=");
        b.append(cueIndex);
        b.append(" , and channelIndex");
        b.append(channelIndex);
        b.append(")");
        String message = b.toString();
        assertLevelValue(message, expected, value);
    }
