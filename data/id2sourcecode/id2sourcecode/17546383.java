    public static WaveData create(AudioInputStream ais) {
        AudioFormat audioformat = ais.getFormat();
        WaveData wavedata = createFormatData(audioformat);
        byte[] buf = new byte[audioformat.getChannels() * (int) ais.getFrameLength() * audioformat.getSampleSizeInBits() / 8];
        int read = 0, total = 0;
        try {
            while ((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length) {
                total += read;
            }
        } catch (IOException ioe) {
            return null;
        }
        ByteBuffer buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16);
        wavedata.data = buffer;
        try {
            ais.close();
        } catch (IOException ioe) {
        }
        return wavedata;
    }
