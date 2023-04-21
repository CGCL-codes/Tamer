    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("SDLAudioSpec[freq=").append(getFreq()).append(", format=").append(getFormat()).append(", channels=").append(getChannels()).append(", silence=").append(getSilence()).append(", samples=").append(getSamples()).append(", size=").append(getSize()).append("]");
        return buf.toString();
    }
