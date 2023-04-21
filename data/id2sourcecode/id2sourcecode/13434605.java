    public float getChannelN(int channel) {
        switch(channel) {
            case RED_CHANNEL:
                return getRedN();
            case GREEN_CHANNEL:
                return getGreenN();
            case BLUE_CHANNEL:
                return getBlueN();
        }
        return 0;
    }
