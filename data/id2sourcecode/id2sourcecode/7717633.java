    private AudioFileFormat getAudioFileFormat(AudioFileFormat.Type type, AudioInputStream stream) {
        AudioFormat format = null;
        AuFileFormat fileFormat = null;
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        AudioFormat streamFormat = stream.getFormat();
        AudioFormat.Encoding streamEncoding = streamFormat.getEncoding();
        float sampleRate;
        int sampleSizeInBits;
        int channels;
        int frameSize;
        float frameRate;
        int fileSize;
        if (!types[0].equals(type)) {
            throw new IllegalArgumentException("File type " + type + " not supported.");
        }
        if ((AudioFormat.Encoding.ALAW.equals(streamEncoding)) || (AudioFormat.Encoding.ULAW.equals(streamEncoding))) {
            encoding = streamEncoding;
            sampleSizeInBits = streamFormat.getSampleSizeInBits();
        } else if (streamFormat.getSampleSizeInBits() == 8) {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
            sampleSizeInBits = 8;
        } else {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
            sampleSizeInBits = streamFormat.getSampleSizeInBits();
        }
        format = new AudioFormat(encoding, streamFormat.getSampleRate(), sampleSizeInBits, streamFormat.getChannels(), streamFormat.getFrameSize(), streamFormat.getFrameRate(), true);
        if (stream.getFrameLength() != AudioSystem.NOT_SPECIFIED) {
            fileSize = (int) stream.getFrameLength() * streamFormat.getFrameSize() + AuFileFormat.AU_HEADERSIZE;
        } else {
            fileSize = AudioSystem.NOT_SPECIFIED;
        }
        fileFormat = new AuFileFormat(AudioFileFormat.Type.AU, fileSize, format, (int) stream.getFrameLength());
        return fileFormat;
    }
