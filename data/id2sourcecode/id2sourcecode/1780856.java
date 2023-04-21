    public void convertTo(ChannelFormat format) {
        if (channelFormat == format) return;
        if (format.getCount() == 1) {
            mixDownChannels();
            channelFormat = format;
        } else if (channelFormat.getCount() == 1) {
            int nc = format.getCount();
            int ns = getSampleCount();
            float[] samples = getChannel(0);
            float gain = 1f / nc;
            for (int s = 0; s < ns; s++) {
                samples[s] *= gain;
            }
            expandChannel(nc);
            channelFormat = format;
        } else {
            @SuppressWarnings("unused") ChannelFormat convertingFormat = channelFormat.getCount() > format.getCount() ? channelFormat : format;
        }
    }
