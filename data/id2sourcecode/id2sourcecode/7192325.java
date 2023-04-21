    public void init(IURLProtocolHandler channel, boolean decodeVideo, boolean decodeAudio) {
        init();
        container = IContainer.make();
        container.setReadRetryCount(-1);
        if (container.open(channel, IContainer.Type.READ, null) < 0) {
            log.error("MediaEngine: Invalid container format!");
        }
        numStreams = container.getNumStreams();
        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (videoStreamID == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                videoStreamID = i;
                videoCoder = coder;
            } else if (audioStreamID == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                audioStreamID = i;
                audioCoder = coder;
            }
        }
        if (decodeVideo) {
            if (videoStreamID == -1) {
                log.error("MediaEngine: No video streams found!");
            } else if (videoCoder.open(null, null) < 0) {
                videoCoder.delete();
                videoCoder = null;
                log.error("MediaEngine: Can't open video decoder!");
            } else {
                videoConverter = ConverterFactory.createConverter(ConverterFactory.XUGGLER_BGR_24, videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());
                videoPicture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());
                if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
                    videoResampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24, videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
                    videoPicture = IVideoPicture.make(videoResampler.getOutputPixelFormat(), videoPicture.getWidth(), videoPicture.getHeight());
                }
                videoStreamState = new StreamState(this, videoStreamID, container, 0);
            }
        }
        if (decodeAudio) {
            if (audioStreamID == -1) {
                if (!initExtAudio()) {
                    log.error("MediaEngine: No audio streams found!");
                    audioStreamState = new StreamState(this, -1, null, sceMpeg.audioFirstTimestamp);
                }
            } else if (audioCoder.open(null, null) < 0) {
                audioCoder.delete();
                audioCoder = null;
                log.error("MediaEngine: Can't open audio decoder!");
            } else {
                audioSamples = IAudioSamples.make(getAudioSamplesSize(), audioCoder.getChannels());
                decodedAudioSamples = new FIFOByteBuffer();
                audioStreamState = new StreamState(this, audioStreamID, container, 0);
            }
        }
    }
