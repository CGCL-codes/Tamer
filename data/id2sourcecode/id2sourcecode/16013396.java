    public void getChannelGains(float[] dest) {
        switch(channelCount) {
            case 6:
                dest[5] = gain;
                dest[4] = gain;
            case 4:
                final float r = gain * rear;
                dest[3] = r * right;
                dest[2] = r * left;
                final float f = gain * front;
                dest[1] = f * right;
                dest[0] = f * left;
                break;
            case 2:
                dest[1] = gain * right;
                dest[0] = gain * left;
                break;
            case 1:
                dest[0] = gain;
                break;
        }
    }
