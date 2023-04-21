    private void prepareAudioStream(File audioFile) throws IOException, UnsupportedAudioFileException, UnavailableStreamSizeException, LowSampleRateException, UnallowedSampleSizeException, StreamLengthOutOfBoundsException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        if (audioInputStream.getFrameLength() == AudioSystem.NOT_SPECIFIED || audioInputStream.getFormat().getFrameSize() == AudioSystem.NOT_SPECIFIED) {
            throw new UnavailableStreamSizeException();
        }
        if (audioInputStream.getFormat().getSampleRate() < ConfigManager.getAudioMinSampleRate()) {
            throw new LowSampleRateException();
        }
        if (Arrays.binarySearch(ConfigManager.getAudioAvailableSampleSizes(), audioInputStream.getFormat().getSampleSizeInBits()) == -1) {
            throw new UnallowedSampleSizeException();
        }
        if (audioInputStream.getFrameLength() > ConfigManager.getAudioMaxFrameLength()) {
            throw new StreamLengthOutOfBoundsException();
        }
        this.audioFormat = audioInputStream.getFormat();
        if (this.audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED && this.audioFormat.getEncoding() != AudioFormat.Encoding.PCM_UNSIGNED) {
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.audioFormat.getSampleRate(), this.audioFormat.getSampleSizeInBits(), this.audioFormat.getChannels(), this.audioFormat.getFrameSize(), this.audioFormat.getFrameRate(), this.audioFormat.isBigEndian());
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
            this.audioFormat = targetFormat;
        }
        this.audioInputStream = audioInputStream;
        this.randomAccessAudioStreamController = new UIOStreamBuilder(this.audioInputStream).setByteOrder(RandomAccessInput.LITTLE_ENDIAN).create();
        this.randomAccessAudioStreamMap = new HashMap<Object, RandomAccessInput>();
        this.audioInputStreamLength = this.randomAccessAudioStreamController.length();
        this.durationInMicroseconds = (long) (((double) this.audioInputStream.getFrameLength() / (double) this.audioFormat.getSampleRate()) * 1000000);
    }
