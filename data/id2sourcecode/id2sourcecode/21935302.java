    public String prettyPrint() {
        StringBuffer result = new StringBuffer(super.prettyPrint().replaceAll(Utils.LINE_SEPARATOR, Utils.LINE_SEPARATOR + "   "));
        result.insert(0, Utils.LINE_SEPARATOR + "AudioStream");
        result.append("Audio info:" + Utils.LINE_SEPARATOR);
        result.append("      Bitrate : " + getKbps() + Utils.LINE_SEPARATOR);
        result.append("      Channels : " + getChannelCount() + " at " + getSamplingRate() + " Hz" + Utils.LINE_SEPARATOR);
        result.append("      Bits per Sample: " + getBitsPerSample() + Utils.LINE_SEPARATOR);
        result.append("      Formatcode: " + getCodecDescription() + Utils.LINE_SEPARATOR);
        return result.toString();
    }
