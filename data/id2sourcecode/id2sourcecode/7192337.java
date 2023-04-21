    private boolean initExtAudio(String file) {
        extContainer = IContainer.make();
        if (log.isDebugEnabled()) {
            log.debug(String.format("initExtAudio %s", file));
        }
        IURLProtocolHandler fileProtocolHandler = new FileProtocolHandler(file);
        if (extContainer.open(fileProtocolHandler, IContainer.Type.READ, null) < 0) {
            log.error("MediaEngine: Invalid file or container format: " + file);
            extContainer.close();
            extContainer = null;
            return false;
        }
        int extNumStreams = extContainer.getNumStreams();
        audioStreamID = -1;
        audioCoder = null;
        for (int i = 0; i < extNumStreams; i++) {
            IStream stream = extContainer.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (audioStreamID == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                audioStreamID = i;
                audioCoder = coder;
            }
        }
        if (audioStreamID == -1) {
            log.error("MediaEngine: No audio streams found in external audio!");
            extContainer.close();
            extContainer = null;
            return false;
        } else if (audioCoder.open(null, null) < 0) {
            log.error("MediaEngine: Can't open audio decoder!");
            extContainer.close();
            extContainer = null;
            return false;
        }
        audioSamples = IAudioSamples.make(getAudioSamplesSize(), audioCoder.getChannels());
        decodedAudioSamples = new FIFOByteBuffer();
        audioStreamState = new StreamState(this, audioStreamID, extContainer, sceMpeg.audioFirstTimestamp);
        audioStreamState.setTimestamps(sceMpeg.mpegTimestampPerSecond);
        log.info(String.format("Using external audio '%s'", file));
        return true;
    }
