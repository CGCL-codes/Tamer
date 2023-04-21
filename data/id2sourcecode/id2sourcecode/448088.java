    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        if (args.length == 1) {
            if (args[0].equals("-h")) {
                printUsageAndExit();
            } else {
                printUsageAndExit();
            }
        } else if (args.length != 3) {
            printUsageAndExit();
        }
        float fTargetSampleRate = Float.parseFloat(args[0]);
        if (DEBUG) {
            out("target sample rate: " + fTargetSampleRate);
        }
        File sourceFile = new File(args[1]);
        File targetFile = new File(args[2]);
        AudioFileFormat sourceFileFormat = AudioSystem.getAudioFileFormat(sourceFile);
        AudioFileFormat.Type targetFileType = sourceFileFormat.getType();
        AudioInputStream sourceStream = null;
        sourceStream = AudioSystem.getAudioInputStream(sourceFile);
        if (sourceStream == null) {
            out("cannot open source audio file: " + sourceFile);
            System.exit(1);
        }
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (DEBUG) {
            out("source format: " + sourceFormat);
        }
        AudioFormat.Encoding encoding = sourceFormat.getEncoding();
        if (!AudioCommon.isPcm(encoding)) {
            out("encoding of source audio data is not PCM; conversion not possible");
            System.exit(1);
        }
        float fTargetFrameRate = fTargetSampleRate;
        AudioFormat targetFormat = new AudioFormat(sourceFormat.getEncoding(), fTargetSampleRate, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), sourceFormat.getFrameSize(), fTargetFrameRate, sourceFormat.isBigEndian());
        if (DEBUG) {
            out("desired target format: " + targetFormat);
        }
        AudioInputStream targetStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
        if (DEBUG) {
            out("targetStream: " + targetStream);
        }
        int nWrittenBytes = 0;
        nWrittenBytes = AudioSystem.write(targetStream, targetFileType, targetFile);
        if (DEBUG) {
            out("Written bytes: " + nWrittenBytes);
        }
    }
