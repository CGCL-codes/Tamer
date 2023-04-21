    public void operate(AChannelSelection ch1) {
        ch1.getChannel().markChange();
        switch(mode) {
            case MIRROR_LEFT_SIDE:
                mirrorLeft(ch1);
                break;
            case MIRROR_RIGHT_SIDE:
                mirrorRight(ch1);
                break;
            case REVERSE:
                reverse(ch1);
                break;
        }
    }
