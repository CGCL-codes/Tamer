    ChannelDevice getChannelDevice(int number) {
        ChannelDevice device = null;
        if (0 <= number && number < channels.length) {
            device = channels[number].getChannelDevice();
        }
        return device;
    }
