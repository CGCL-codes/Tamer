    private int getChannel(int sensor) {
        int channel;
        channel = si[sensor].getTi().getChannel();
        return channel;
    }
