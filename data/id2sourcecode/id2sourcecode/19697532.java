    private String generateInstrumentText() {
        StringBuffer iText = new StringBuffer();
        String channelVariables = getChannelVariables();
        if (channelVariables == null) {
            return null;
        }
        iText.append(channelVariables).append("\tdiskin2\tp4, 1, p5\n");
        if (this.numChannels == 1) {
            iText.append("\tout\t").append(channelVariables);
        } else {
            iText.append("\toutc\t").append(channelVariables);
        }
        return iText.toString();
    }
