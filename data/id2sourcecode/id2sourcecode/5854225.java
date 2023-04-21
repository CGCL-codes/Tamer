    private int getChannels() {
        String strChannels = getResourceString(getResourcePrefix() + ".format.channels");
        int nChannels = Integer.parseInt(strChannels);
        return nChannels;
    }
