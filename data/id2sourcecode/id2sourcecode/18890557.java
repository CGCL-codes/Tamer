    public ADPCMDecoder(FFXISound sound, ReadableByteChannel in) {
        channels = sound.getChannels();
        if (sound.getBitsPerSample() != 16) throw new UnsupportedFormatException();
        if (sound.getLength() % 16 != 0) throw new UnsupportedFormatException();
        frameSize = channels * 9;
        loopOffset = sound.getLoopPoint() * frameSize;
        residual = new short[channels][2];
        buffer = ByteBuffer.allocateDirect(Sound.BUFFER_SIZE * channels / 16 * 9);
    }
