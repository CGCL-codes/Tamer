    public MidiChannel[] getChannels() {
        synchronized (control_mutex) {
            if (external_channels == null) {
                external_channels = new SoftChannelProxy[16];
                for (int i = 0; i < external_channels.length; i++) external_channels[i] = new SoftChannelProxy();
            }
            MidiChannel[] ret;
            if (isOpen()) ret = new MidiChannel[channels.length]; else ret = new MidiChannel[16];
            for (int i = 0; i < ret.length; i++) ret[i] = external_channels[i];
            return ret;
        }
    }
