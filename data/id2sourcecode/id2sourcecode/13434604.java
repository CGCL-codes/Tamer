    public int getChannel(int channel) {
        switch(channel) {
            case ALL_CHANNELS:
                return getPixelColor();
            case RED_CHANNEL:
                return getRed();
            case GREEN_CHANNEL:
                return getGreen();
            case BLUE_CHANNEL:
                return getBlue();
        }
        return 0;
    }
