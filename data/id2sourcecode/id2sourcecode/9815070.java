    private AudioFileFormat getAudioFileFormat(AudioFileFormat.Type type, AudioInputStream stream) {
        AudioFormat format = null;
        AiffFileFormat fileFormat = null;
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        AudioFormat streamFormat = stream.getFormat();
        AudioFormat.Encoding streamEncoding = streamFormat.getEncoding();
        float sampleRate;
        int sampleSizeInBits;
        int channels;
        int frameSize;
        float frameRate;
        int fileSize;
        boolean convert8to16 = false;
        if (!types[0].equals(type)) {
            throw new IllegalArgumentException("File type " + type + " not supported.");
        }
        if ((AudioFormat.Encoding.ALAW.equals(streamEncoding)) || (AudioFormat.Encoding.ULAW.equals(streamEncoding))) {
            if (streamFormat.getSampleSizeInBits() == 8) {
                encoding = AudioFormat.Encoding.PCM_SIGNED;
                sampleSizeInBits = 16;
                convert8to16 = true;
            } else {
                throw new IllegalArgumentException("Encoding " + streamEncoding + " supported only for 8-bit data.");
            }
        } else if (streamFormat.getSampleSizeInBits() == 8) {
            encoding = AudioFormat.Encoding.PCM_UNSIGNED;
            sampleSizeInBits = 8;
        } else {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
            sampleSizeInBits = streamFormat.getSampleSizeInBits();
        }
        format = new AudioFormat(encoding, streamFormat.getSampleRate(), sampleSizeInBits, streamFormat.getChannels(), streamFormat.getFrameSize(), streamFormat.getFrameRate(), true);
        if (stream.getFrameLength() != AudioSystem.NOT_SPECIFIED) {
            if (convert8to16) {
                fileSize = (int) stream.getFrameLength() * streamFormat.getFrameSize() * 2 + AiffFileFormat.AIFF_HEADERSIZE;
            } else {
                fileSize = (int) stream.getFrameLength() * streamFormat.getFrameSize() + AiffFileFormat.AIFF_HEADERSIZE;
            }
        } else {
            fileSize = AudioSystem.NOT_SPECIFIED;
        }
        fileFormat = new AiffFileFormat(AudioFileFormat.Type.AIFF, fileSize, format, (int) stream.getFrameLength());
        return fileFormat;
    }
