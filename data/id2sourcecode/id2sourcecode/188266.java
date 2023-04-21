    public static AudioInputStream getConvertedStream(AudioInputStream sourceStream, AudioFormat.Encoding targetEncoding) throws Exception {
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (!quiet) {
            System.out.println("Input format: " + sourceFormat);
        }
        AudioInputStream targetStream = null;
        if (!AudioSystem.isConversionSupported(targetEncoding, sourceFormat)) {
            if (DEBUG && !quiet) {
                System.out.println("Direct conversion not possible.");
                System.out.println("Trying with intermediate PCM format.");
            }
            AudioFormat intermediateFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), 2 * sourceFormat.getChannels(), sourceFormat.getSampleRate(), false);
            if (AudioSystem.isConversionSupported(intermediateFormat, sourceFormat)) {
                sourceStream = AudioSystem.getAudioInputStream(intermediateFormat, sourceStream);
            }
        }
        targetStream = AudioSystem.getAudioInputStream(targetEncoding, sourceStream);
        if (targetStream == null) {
            throw new Exception("conversion not supported");
        }
        if (!quiet) {
            if (DEBUG) {
                System.out.println("Got converted AudioInputStream: " + targetStream.getClass().getName());
            }
            System.out.println("Output format: " + targetStream.getFormat());
        }
        return targetStream;
    }
