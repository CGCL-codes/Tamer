    public static WaveData create(AudioInputStream ais) {
        AudioFormat audioformat = ais.getFormat();
        int channels = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL.FORMAT_MONO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL.FORMAT_MONO16;
            } else {
                assert false : "Illegal sample size";
            }
        } else if (audioformat.getChannels() == 2) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL.FORMAT_STEREO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL.FORMAT_STEREO16;
            } else {
                assert false : "Illegal sample size";
            }
        } else {
            assert false : "Only mono or stereo is supported";
        }
        byte[] buf = new byte[audioformat.getChannels() * (int) ais.getFrameLength() * audioformat.getSampleSizeInBits() / 8];
        int read = 0, total = 0;
        try {
            while ((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length) {
                total += read;
            }
        } catch (IOException ioe) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(buf.length);
        buffer.put(buf);
        WaveData wavedata = new WaveData(buffer, channels, (int) audioformat.getSampleRate());
        try {
            ais.close();
        } catch (IOException ioe) {
        }
        return wavedata;
    }
