    private void assertChannelDerived(final int cueIndex, final int channelIndex, final boolean expected) {
        boolean derived = getDetail(cueIndex).getChannelLevel(channelIndex).isDerived();
        StringBuilder b = new StringBuilder();
        b.append("(channelLevel.isDerived() at cueIndex=");
        b.append(cueIndex);
        b.append(" , and channelIndex");
        b.append(channelIndex);
        b.append(")");
        String message = b.toString();
        assertTrue(expected == derived, message);
    }
