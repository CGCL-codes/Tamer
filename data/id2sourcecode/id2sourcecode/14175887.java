    public DecodeAudio(String filename) {
        IContainer container = IContainer.make();
        if (container.open(filename, IContainer.Type.READ, null) < 0) throw new IllegalArgumentException("could not open file: " + filename);
        int numStreams = container.getNumStreams();
        int audioStreamId = -1;
        IStreamCoder audioCoder = null;
        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                audioStreamId = i;
                audioCoder = coder;
                break;
            }
        }
        if (audioStreamId == -1) throw new RuntimeException("could not find audio stream in container: " + filename);
        if (audioCoder.open() < 0) throw new RuntimeException("could not open audio decoder for container: " + filename);
        openJavaSound(audioCoder);
        IPacket packet = IPacket.make();
        while (container.readNextPacket(packet) >= 0) {
            if (packet.getStreamIndex() == audioStreamId) {
                IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());
                int offset = 0;
                while (offset < packet.getSize()) {
                    int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
                    if (bytesDecoded < 0) throw new RuntimeException("got error decoding audio in: " + filename);
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
        closeJavaSound();
        if (audioCoder != null) {
            audioCoder.close();
            audioCoder = null;
        }
        if (container != null) {
            container.close();
            container = null;
        }
    }
