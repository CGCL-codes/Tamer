    public int getChannelRangeMin(int channel) {
        if ((channel >= 0) && (channel < _channels.length)) {
            Channel chan = _channels[channel];
            return (chan.MinChannelValue - (channel * 0x400));
        } else {
            return 0x00;
        }
    }
