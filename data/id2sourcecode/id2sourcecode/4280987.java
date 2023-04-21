    public AudioInputStream convert() throws UnsupportedAudioFileException, IOException {
        File inputFile = new File(this.inputFile);
        AudioFileFormat inputFileFormat = AudioSystem.getAudioFileFormat(inputFile);
        AudioFileFormat.Type defaultFileType = inputFileFormat.getType();
        AudioInputStream stream = null;
        stream = AudioSystem.getAudioInputStream(inputFile);
        AudioFormat format = stream.getFormat();
        System.out.println("source format: " + format);
        AudioFormat targetFormat = null;
        if (desiredEncoding == null) {
            desiredEncoding = format.getEncoding();
        }
        if (desiredSampleRate == AudioSystem.NOT_SPECIFIED) {
            desiredSampleRate = format.getSampleRate();
        }
        if (desiredSampleSizeInBits == AudioSystem.NOT_SPECIFIED) {
            desiredSampleSizeInBits = format.getSampleSizeInBits();
        }
        if (desiredChannels == AudioSystem.NOT_SPECIFIED) {
            desiredChannels = format.getChannels();
        }
        if (desiredBigEndian == null) desiredBigEndian = format.isBigEndian();
        if (!AudioCommon.isPcm(format.getEncoding())) {
            System.out.println("converting to PCM...");
            AudioFormat.Encoding targetEncoding = (format.getSampleSizeInBits() == 8) ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED;
            stream = convertEncoding(targetEncoding, stream);
            System.out.println("stream: " + stream);
            System.out.println("format: " + stream.getFormat());
            if (desiredSampleSizeInBits == AudioSystem.NOT_SPECIFIED) {
                desiredSampleSizeInBits = format.getSampleSizeInBits();
            }
        }
        if (stream.getFormat().getChannels() != desiredChannels) {
            System.out.println("converting channels...");
            stream = convertChannels(desiredChannels, stream);
            System.out.println("stream: " + stream);
            System.out.println("format: " + stream.getFormat());
        }
        boolean bDoConvertSampleSize = (stream.getFormat().getSampleSizeInBits() != desiredSampleSizeInBits);
        boolean bDoConvertEndianess = (stream.getFormat().isBigEndian() != desiredBigEndian);
        if (bDoConvertSampleSize || bDoConvertEndianess) {
            System.out.println("converting sample size and endianess...");
            stream = convertSampleSizeAndEndianess(desiredSampleSizeInBits, desiredBigEndian, stream);
            System.out.println("stream: " + stream);
            System.out.println("format: " + stream.getFormat());
        }
        if (!equals(stream.getFormat().getSampleRate(), desiredSampleRate)) {
            System.out.println("converting sample rate...");
            stream = convertSampleRate(desiredSampleRate, stream);
            System.out.println("stream: " + stream);
            System.out.println("format: " + stream.getFormat());
        }
        if (!stream.getFormat().getEncoding().equals(desiredEncoding)) {
            System.out.println("converting to " + desiredEncoding + "...");
            stream = convertEncoding(desiredEncoding, stream);
            System.out.println("stream: " + stream);
            System.out.println("format: " + stream.getFormat());
        }
        return stream;
    }
