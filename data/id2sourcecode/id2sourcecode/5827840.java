    public static WaveData create(AudioInputStream ais) {
        AudioFormat audioformat = ais.getFormat();
        int channels = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL10.AL_FORMAT_MONO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL10.AL_FORMAT_MONO16;
            } else {
                assert false : "Illegal sample size";
            }
        } else if (audioformat.getChannels() == 2) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL10.AL_FORMAT_STEREO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL10.AL_FORMAT_STEREO16;
            } else {
                assert false : "Illegal sample size";
            }
        } else {
            assert false : "Only mono or stereo is supported";
        }
        ByteBuffer buffer = null;
        try {
            int available = ais.available();
            if (available <= 0) {
                available = ais.getFormat().getChannels() * (int) ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;
            }
            byte[] buf = new byte[ais.available()];
            int read = 0, total = 0;
            while ((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length) {
                total += read;
            }
            buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16);
        } catch (IOException ioe) {
            return null;
        }
        WaveData wavedata = new WaveData(buffer, channels, (int) audioformat.getSampleRate());
        try {
            ais.close();
        } catch (IOException ioe) {
        }
        return wavedata;
    }
