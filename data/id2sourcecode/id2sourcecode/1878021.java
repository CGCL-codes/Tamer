    public static WaveData create(AudioInputStream ais) {
        AudioFormat audioformat = ais.getFormat();
        int channels = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL10.AL_FORMAT_MONO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL10.AL_FORMAT_MONO16;
            } else {
                System.out.println("WaveData.create(): Illegal sample size");
            }
        } else if (audioformat.getChannels() == 2) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL10.AL_FORMAT_STEREO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL10.AL_FORMAT_STEREO16;
            } else {
                System.out.println("WaveData.create(): Illegal sample size");
            }
        } else {
            System.out.println("WaveData.create(): Only mono or stereo is supported");
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
        ByteBuffer buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16);
        WaveData wavedata = new WaveData(buffer, channels, (int) audioformat.getSampleRate());
        try {
            ais.close();
        } catch (IOException ioe) {
        }
        return wavedata;
    }
