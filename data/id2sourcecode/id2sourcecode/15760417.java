    private WavVectorDataSet(ByteBuffer dataBuf, AudioFormat format) throws IOException {
        this.audioFormat = format;
        this.byteBuffer = dataBuf;
        channelCount = audioFormat.getChannels();
        channel = 0;
        sampleRate = audioFormat.getSampleRate();
        frameSize = audioFormat.getFrameSize();
        unsigned = audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
        frameCount = (byteBuffer.limit() - byteBuffer.position()) / frameSize;
        if (audioFormat.getSampleSizeInBits() == 16) {
            if (audioFormat.isBigEndian()) {
                byteBuffer.order(ByteOrder.BIG_ENDIAN);
            } else {
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            }
            shortBuffer = byteBuffer.asShortBuffer();
        }
    }
