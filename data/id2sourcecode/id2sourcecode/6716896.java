    public long getDownstreamBandwidth() {
        long[] channelBandwidth = getChannelBandwidth();
        if (channelBandwidth[IBandwidthConfigure.OVERALL_CHANNEL] >= 0) {
            return channelBandwidth[IBandwidthConfigure.OVERALL_CHANNEL];
        } else {
            long bw = 0;
            if (channelBandwidth[IBandwidthConfigure.AUDIO_CHANNEL] < 0 || channelBandwidth[IBandwidthConfigure.VIDEO_CHANNEL] < 0) {
                bw = -1;
            } else {
                bw = channelBandwidth[IBandwidthConfigure.AUDIO_CHANNEL] + channelBandwidth[IBandwidthConfigure.VIDEO_CHANNEL];
            }
            if (channelBandwidth[IBandwidthConfigure.DATA_CHANNEL] >= 0) {
                bw += channelBandwidth[IBandwidthConfigure.DATA_CHANNEL];
            }
            return bw;
        }
    }
