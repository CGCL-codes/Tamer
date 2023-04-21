    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        result.append(prefix).append("  |-> Audio info:").append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |  : Bitrate : ").append(getKbps()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |  : Channels : ").append(getChannelCount()).append(" at ").append(getSamplingRate()).append(" Hz").append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |  : Bits per Sample: ").append(getBitsPerSample()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |  : Formatcode: ").append(getCodecDescription()).append(Utils.LINE_SEPARATOR);
        return result.toString();
    }
