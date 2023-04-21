    public AudioInFloatSampled(AudioInputStream ais, int signal_length) {
        this.SIGNAL_LENGTH = signal_length;
        this.src_format = ais.getFormat();
        AudioFormat format = ais.getFormat();
        if ((format.isBigEndian() == true) && (format.getEncoding().equals(Encoding.PCM_SIGNED))) {
            this.dis = new DataInputStream(new BufferedInputStream(ais));
        } else {
            int size_in_bits = format.getSampleSizeInBits();
            if (size_in_bits != 8 && size_in_bits != 16) size_in_bits = 16;
            format = new AudioFormat(format.getSampleRate(), size_in_bits, format.getChannels(), true, true);
            this.dis = new DataInputStream(new BufferedInputStream(AudioSystem.getAudioInputStream(format, ais)));
        }
        this.is_mono = format.getChannels() == 1;
        this.sample_rate = format.getSampleRate();
        this.bits_per_sample = format.getSampleSizeInBits();
    }
