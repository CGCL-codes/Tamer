    private void create(AudioInputStream ais) throws AudioDataException {
        AudioFormat audioformat = ais.getFormat();
        int channels = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL.AL_FORMAT_MONO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL.AL_FORMAT_MONO16;
            } else {
                throw new AudioDataException("Illegal sample size");
            }
        } else if (audioformat.getChannels() == 2) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL.AL_FORMAT_STEREO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL.AL_FORMAT_STEREO16;
            } else {
                throw new AudioDataException("Illegal sample size");
            }
        } else {
            throw new AudioDataException("Only mono or stereo supported");
        }
        byte[] buf = new byte[audioformat.getChannels() * (int) ais.getFrameLength() * audioformat.getSampleSizeInBits() / 8];
        int read = 0, total = 0;
        try {
            while ((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length) {
                total += read;
            }
        } catch (IOException ioe) {
            throw new AudioDataException("IO Error", ioe);
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(buf.length);
        buffer.put(buf);
        buffer.rewind();
        data = buffer;
        format = channels;
        samplerate = (int) audioformat.getSampleRate();
        try {
            ais.close();
        } catch (IOException ioe) {
        }
    }
