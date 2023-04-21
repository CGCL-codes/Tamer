    public void setDefaultChannelNames() {
        int nChannels = getNChannels();
        for (int c = 1; c <= nChannels; c++) {
            getChannelCalibration(c).setLabel("Ch-" + c);
        }
    }
