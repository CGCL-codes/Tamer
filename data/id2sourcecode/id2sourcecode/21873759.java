    private static WaveData loadFromAudioInputStream(AudioInputStream aIn) throws UnsupportedAudioFileException, IOException {
        WaveData result = null;
        ReadableByteChannel aChannel = Channels.newChannel(aIn);
        AudioFormat fmt = aIn.getFormat();
        int numChannels = fmt.getChannels();
        int bits = fmt.getSampleSizeInBits();
        BufferFormat format = null;
        try {
            format = BufferFormat.getFromValues(bits, numChannels);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            format = BufferFormat.MONO8;
        }
        int freq = Math.round(fmt.getSampleRate());
        int size = aIn.available();
        ByteBuffer buffer = BufferUtils.createByteBuffer(size);
        aChannel.read(buffer);
        result = new WaveData(buffer, format, size, freq, false);
        aIn.close();
        return (result);
    }
