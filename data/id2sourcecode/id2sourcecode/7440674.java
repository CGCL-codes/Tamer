    public TGChannel getFreeChannel(short instrument, boolean isPercussion) {
        if (isPercussion) {
            return TGChannel.newPercussionChannel(getFactory());
        }
        short normalChannel = -1;
        short effectChannel = -1;
        boolean[] usedChannels = getUsedChannels();
        boolean[] usedEffectChannels = getUsedEffectChannels();
        for (short i = 0; i < MAX_CHANNELS; i++) {
            if (!TGChannel.isPercussionChannel(i) && !usedChannels[i] && !usedEffectChannels[i]) {
                normalChannel = (normalChannel < 0) ? i : normalChannel;
                effectChannel = (effectChannel < 0 && i != normalChannel) ? i : effectChannel;
            }
        }
        if (normalChannel < 0 || effectChannel < 0) {
            if (normalChannel >= 0) {
                effectChannel = normalChannel;
            } else {
                TGChannel songChannel = getLastTrack().getChannel();
                return songChannel.clone(getFactory());
            }
        }
        TGChannel channel = getFactory().newChannel();
        channel.setChannel(normalChannel);
        channel.setEffectChannel(effectChannel);
        channel.setInstrument(instrument);
        return channel;
    }
