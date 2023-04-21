    @SuppressWarnings("deprecation")
    public DecodeAudioAndVideo(String filename) {
        if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) throw new RuntimeException("You must install the GPL version of Xuggler (with IVideoResampler support) for this demo to work");
        IContainer container = IContainer.make();
        if (container.open(filename, IContainer.Type.READ, null) < 0) throw new IllegalArgumentException("Could not open file: " + filename);
        int numStreams = container.getNumStreams();
        int videoStreamId = -1;
        IStreamCoder videoCoder = null;
        int audioStreamId = -1;
        IStreamCoder audioCoder = null;
        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (videoStreamId == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                videoStreamId = i;
                videoCoder = coder;
            } else if (audioStreamId == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                audioStreamId = i;
                audioCoder = coder;
            }
        }
        if (videoStreamId == -1 && audioStreamId == -1) throw new RuntimeException("Could not find audio or video stream in container: " + filename);
        IVideoResampler resampler = null;
        if (videoCoder != null) {
            if (videoCoder.open() < 0) throw new RuntimeException("Could not open audio decoder for container: " + filename);
            if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
                resampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24, videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
                if (resampler == null) throw new RuntimeException("Could not create color space resampler for: " + filename);
            }
            openJavaVideo();
        }
        if (audioCoder != null) {
            if (audioCoder.open() < 0) throw new RuntimeException("Could not open audio decoder for container: " + filename);
            try {
                openJavaSound(audioCoder);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException("Unable to open sound device on your system when playing back container: " + filename);
            }
        }
        IPacket packet = IPacket.make();
        mFirstVideoTimestampInStream = Global.NO_PTS;
        mSystemVideoClockStartTime = 0;
        while (container.readNextPacket(packet) >= 0) {
            if (packet.getStreamIndex() == videoStreamId) {
                IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());
                int bytesDecoded = videoCoder.decodeVideo(picture, packet, 0);
                if (bytesDecoded < 0) throw new RuntimeException("Got error decoding audio in: " + filename);
                if (picture.isComplete()) {
                    IVideoPicture newPic = picture;
                    if (resampler != null) {
                        newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(), picture.getHeight());
                        if (resampler.resample(newPic, picture) < 0) throw new RuntimeException("Could not resample video from: " + filename);
                    }
                    if (newPic.getPixelType() != IPixelFormat.Type.BGR24) throw new RuntimeException("Could not decode video as BGR 24 bit data in: " + filename);
                    long delay = millisecondsUntilTimeToDisplay(newPic);
                    try {
                        if (delay > 0) Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        return;
                    }
                    mScreen.setImage(Utils.videoPictureToImage(newPic));
                }
            } else if (packet.getStreamIndex() == audioStreamId) {
                IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());
                int offset = 0;
                while (offset < packet.getSize()) {
                    int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
                    if (bytesDecoded < 0) throw new RuntimeException("Got error decoding audio in: " + filename);
                    offset += bytesDecoded;
                    if (samples.isComplete()) {
                        playJavaSound(samples);
                    }
                }
            } else {
                do {
                } while (false);
            }
        }
        if (videoCoder != null) {
            videoCoder.close();
            videoCoder = null;
        }
        if (audioCoder != null) {
            audioCoder.close();
            audioCoder = null;
        }
        if (container != null) {
            container.close();
            container = null;
        }
        closeJavaSound();
        closeJavaVideo();
    }
