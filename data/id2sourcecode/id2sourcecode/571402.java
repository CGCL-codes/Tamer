    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        int nDesiredChannels = AudioSystem.NOT_SPECIFIED;
        int nDesiredSampleSizeInBits = AudioSystem.NOT_SPECIFIED;
        AudioFormat.Encoding desiredEncoding = null;
        float fDesiredSampleRate = AudioSystem.NOT_SPECIFIED;
        AudioFileFormat.Type desiredFileType = null;
        boolean bDesiredBigEndian = false;
        boolean bIsEndianessDesired = false;
        Getopt g = new Getopt("AudioConverter", args, "hlc:s:e:f:t:BLD");
        int c;
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'h':
                    printUsageAndExit();
                case 'l':
                    AudioCommon.listSupportedTargetTypes();
                    System.exit(0);
                case 'c':
                    nDesiredChannels = Integer.parseInt(g.getOptarg());
                    break;
                case 's':
                    nDesiredSampleSizeInBits = Integer.parseInt(g.getOptarg());
                    break;
                case 'e':
                    String strEncodingName = g.getOptarg();
                    desiredEncoding = new AudioFormat.Encoding(strEncodingName);
                    break;
                case 'f':
                    fDesiredSampleRate = Float.parseFloat(g.getOptarg());
                    break;
                case 't':
                    String strExtension = g.getOptarg();
                    desiredFileType = AudioCommon.findTargetType(strExtension);
                    if (desiredFileType == null) {
                        out("Unknown target file type. Check with 'AudioConverter -l'.");
                        System.exit(1);
                    }
                    break;
                case 'B':
                    bDesiredBigEndian = true;
                    bIsEndianessDesired = true;
                    break;
                case 'L':
                    bDesiredBigEndian = true;
                    bIsEndianessDesired = true;
                    break;
                case 'D':
                    DEBUG = true;
                    break;
                case '?':
                    printUsageAndExit();
                default:
                    out("getopt() returned " + c);
                    break;
            }
        }
        if (args.length - g.getOptind() < 2) {
            printUsageAndExit();
        }
        File inputFile = new File(args[g.getOptind()]);
        File outputFile = new File(args[g.getOptind() + 1]);
        AudioFileFormat inputFileFormat = AudioSystem.getAudioFileFormat(inputFile);
        AudioFileFormat.Type defaultFileType = inputFileFormat.getType();
        AudioInputStream stream = null;
        stream = AudioSystem.getAudioInputStream(inputFile);
        AudioFormat format = stream.getFormat();
        if (DEBUG) out("source format: " + format);
        AudioFormat targetFormat = null;
        if (desiredEncoding == null) {
            desiredEncoding = format.getEncoding();
        }
        if (fDesiredSampleRate == AudioSystem.NOT_SPECIFIED) {
            fDesiredSampleRate = format.getSampleRate();
        }
        if (nDesiredSampleSizeInBits == AudioSystem.NOT_SPECIFIED) {
            nDesiredSampleSizeInBits = format.getSampleSizeInBits();
        }
        if (nDesiredChannels == AudioSystem.NOT_SPECIFIED) {
            nDesiredChannels = format.getChannels();
        }
        if (!bIsEndianessDesired) {
            bDesiredBigEndian = format.isBigEndian();
        }
        if (!AudioCommon.isPcm(format.getEncoding())) {
            if (DEBUG) out("converting to PCM...");
            AudioFormat.Encoding targetEncoding = (format.getSampleSizeInBits() == 8) ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED;
            stream = convertEncoding(targetEncoding, stream);
            if (DEBUG) out("stream: " + stream);
            if (DEBUG) out("format: " + stream.getFormat());
            if (nDesiredSampleSizeInBits == AudioSystem.NOT_SPECIFIED) {
                nDesiredSampleSizeInBits = format.getSampleSizeInBits();
            }
        }
        if (stream.getFormat().getChannels() != nDesiredChannels) {
            if (DEBUG) out("converting channels...");
            stream = convertChannels(nDesiredChannels, stream);
            if (DEBUG) out("stream: " + stream);
            if (DEBUG) out("format: " + stream.getFormat());
        }
        boolean bDoConvertSampleSize = (stream.getFormat().getSampleSizeInBits() != nDesiredSampleSizeInBits);
        boolean bDoConvertEndianess = (stream.getFormat().isBigEndian() != bDesiredBigEndian);
        if (bDoConvertSampleSize || bDoConvertEndianess) {
            if (DEBUG) out("converting sample size and endianess...");
            stream = convertSampleSizeAndEndianess(nDesiredSampleSizeInBits, bDesiredBigEndian, stream);
            if (DEBUG) out("stream: " + stream);
            if (DEBUG) out("format: " + stream.getFormat());
        }
        if (!equals(stream.getFormat().getSampleRate(), fDesiredSampleRate)) {
            if (DEBUG) out("converting sample rate...");
            stream = convertSampleRate(fDesiredSampleRate, stream);
            if (DEBUG) out("stream: " + stream);
            if (DEBUG) out("format: " + stream.getFormat());
        }
        if (!stream.getFormat().getEncoding().equals(desiredEncoding)) {
            if (DEBUG) out("converting to " + desiredEncoding + "...");
            stream = convertEncoding(desiredEncoding, stream);
            if (DEBUG) out("stream: " + stream);
            if (DEBUG) out("format: " + stream.getFormat());
        }
        int nWrittenBytes = 0;
        AudioFileFormat.Type targetFileType = (desiredFileType != null) ? desiredFileType : defaultFileType;
        nWrittenBytes = AudioSystem.write(stream, targetFileType, outputFile);
        if (DEBUG) out("Written bytes: " + nWrittenBytes);
    }
