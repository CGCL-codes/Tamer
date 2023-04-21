    private static WAVData readFromStream(AudioInputStream aIn) throws UnsupportedAudioFileException, IOException {
        ReadableByteChannel aChannel = Channels.newChannel(aIn);
        AudioFormat fmt = aIn.getFormat();
        int numChannels = fmt.getChannels();
        int bits = fmt.getSampleSizeInBits();
        int format = AL_FORMAT_MONO8;
        if ((bits == 8) && (numChannels == 1)) {
            format = AL_FORMAT_MONO8;
        } else if ((bits == 16) && (numChannels == 1)) {
            format = AL_FORMAT_MONO16;
        } else if ((bits == 8) && (numChannels == 2)) {
            format = AL_FORMAT_STEREO8;
        } else if ((bits == 16) && (numChannels == 2)) {
            format = AL_FORMAT_STEREO16;
        }
        int freq = Math.round(fmt.getSampleRate());
        int size = aIn.available();
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        while (buffer.remaining() > 0) {
            aChannel.read(buffer);
        }
        buffer.rewind();
        if ((bits == 16) && (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN)) {
            int len = buffer.remaining();
            for (int i = 0; i < len; i += 2) {
                byte a = buffer.get(i);
                byte b = buffer.get(i + 1);
                buffer.put(i, b);
                buffer.put(i + 1, a);
            }
        }
        WAVData result = new WAVData(buffer, format, size, freq, false);
        aIn.close();
        return result;
    }
