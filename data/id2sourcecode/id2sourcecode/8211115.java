    @Override
    public void readAudioInternal(List<URL> urls) throws UnsupportedAudioFileException, IOException {
        URL url = urls.get(0);
        AudioInputStream in = AudioSystem.getAudioInputStream(url);
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        Long size = 1L;
        byte[] data = new byte[4096];
        started(size);
        int nBytesRead = 0;
        long index = 0;
        while (nBytesRead != -1) {
            nBytesRead = din.read(data, 0, data.length);
            if (nBytesRead != -1) {
                for (byte readByte : data) {
                    getWraperExtractorReader().put((byte) readByte);
                    processed(index, size);
                    index++;
                }
            }
        }
        getWraperExtractorReader().pushValues();
        din.close();
        ended();
    }
