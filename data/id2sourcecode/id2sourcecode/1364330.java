    private static void addChanneledAudioFormats(Collection<AudioFormat> collection, AudioFormat protoAudioFormat, int nMinChannels, int nMaxChannels) {
        if (TDebug.TraceMixer) {
            TDebug.out("AlsaDataLineMixer.addChanneledAudioFormats(): begin");
        }
        for (int nChannels = nMinChannels; nChannels <= nMaxChannels; nChannels++) {
            AudioFormat channeledAudioFormat = getChanneledAudioFormat(protoAudioFormat, nChannels);
            if (TDebug.TraceMixer) {
                TDebug.out("AlsaDataLineMixer.addChanneledAudioFormats(): adding AudioFormat: " + channeledAudioFormat);
            }
            collection.add(channeledAudioFormat);
        }
        if (TDebug.TraceMixer) {
            TDebug.out("AlsaDataLineMixer.addChanneledAudioFormats(): end");
        }
    }
