    private String generateInstrumentText() {
        StringBuffer iText = new StringBuffer();
        String channelVariables = getChannelVariables();
        if (channelVariables == null) {
            return null;
        }
        String sfName = getSoundFileName().replace('\\', '/');
        iText.append(channelVariables).append("\tdiskin2\t\"");
        iText.append(sfName);
        iText.append("\", 1, p4\n");
        iText.append(getCsoundPostCode());
        return iText.toString();
    }
