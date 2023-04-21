    private static WAVData loadFromAudioInputStream(AudioInputStream aIn) throws UnsupportedAudioFileException, IOException {
        WAVData result = null;
        ReadableByteChannel aChannel = Channels.newChannel(aIn);
        AudioFormat fmt = aIn.getFormat();
        int numChannels = fmt.getChannels();
        int bits = fmt.getSampleSizeInBits();
        BufferFormat format;
        if ((bits == 8) && (numChannels == 1)) {
            format = BufferFormat.MONO8;
        } else if ((bits == 16) && (numChannels == 1)) {
            format = BufferFormat.MONO16;
        } else if ((bits == 8) && (numChannels == 2)) {
            format = BufferFormat.STEREO8;
        } else if ((bits == 16) && (numChannels == 2)) {
            format = BufferFormat.STEREO16;
        } else {
            format = BufferFormat.MONO8;
        }
        int freq = Math.round(fmt.getSampleRate());
        int size = aIn.available();
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        aChannel.read(buffer);
        result = new WAVData(buffer, format, size, freq, false);
        aIn.close();
        return (result);
    }
