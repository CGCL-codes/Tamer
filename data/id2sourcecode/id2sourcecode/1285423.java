    public synchronized int getChannelVolume(int channel) {
        checkState();
        checkChannel(channel);
        if (_player != null && _player.hNative != 0) {
            return nGetChannelVolume(_player.hNative, channel);
        } else {
            return -1;
        }
    }
