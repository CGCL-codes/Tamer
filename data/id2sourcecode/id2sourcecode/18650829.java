    private int getChannel(int sensor, int mod) {
        int channel;
        channel = si[sensor].getCi().getMi()[mod].getChannel();
        return channel;
    }
