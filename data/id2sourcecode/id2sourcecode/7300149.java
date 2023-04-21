    public static AChannelSelection reduceSilence(AChannelSelection s, float silenceThreshold, int minimumWidth) {
        int low = s.getOffset();
        int high = low + s.getLength();
        AChannel ch = s.getChannel();
        low = AOToolkit.getNextUpperSilenceIndex(ch.getSamples(), low, ch.getSamples().getLength() - low, silenceThreshold, minimumWidth);
        high = AOToolkit.getNextLowerSilenceIndex(ch.getSamples(), high, high, silenceThreshold, minimumWidth);
        if (low == -1) {
            low = 0;
        }
        if (high == -1) {
            high = ch.getSamples().getLength() - 1;
        }
        if (low < high) {
            return new AChannelSelection(ch, low, high - low);
        }
        return new AChannelSelection(ch, 0, 0);
    }
