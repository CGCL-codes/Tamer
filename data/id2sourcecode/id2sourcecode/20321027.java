    private synchronized WaveViewSubscription findSubscription(WaveletName waveletName, String channelId) {
        for (WaveViewSubscription subscription : subscriptions.get(waveletName.waveId)) {
            if (subscription.includes(waveletName.waveletId)) {
                if (subscription.getChannelId().equals(channelId)) {
                    return subscription;
                }
            }
        }
        return null;
    }
