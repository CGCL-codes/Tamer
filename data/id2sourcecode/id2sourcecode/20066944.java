    @Override
    public void onWaveletUpdate(ProtocolWaveletUpdate message) {
        WaveletName wavelet = deserialize(message.getWaveletName());
        WaveWebSocketCallback stream = streams.get(wavelet.waveId);
        if (stream != null) {
            boolean drop;
            String knownChannelId = knownChannels.get(wavelet.waveId);
            if (knownChannelId != null) {
                drop = message.hasChannelId() && !message.getChannelId().equals(knownChannelId);
            } else {
                if (message.hasChannelId()) {
                    knownChannels.put(wavelet.waveId, message.getChannelId());
                }
                drop = false;
            }
            if (!drop) {
                stream.onWaveletUpdate(message);
            }
        } else {
        }
    }
