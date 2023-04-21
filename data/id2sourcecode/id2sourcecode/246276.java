    public static void main(String[] args) throws Exception {
        boolean bInterpretFilenameAsUrl = false;
        boolean bForceConversion = false;
        boolean bBigEndian = false;
        int nSampleSizeInBits = 16;
        String strMixerName = null;
        int nExternalBufferSize = DEFAULT_EXTERNAL_BUFFER_SIZE;
        int nInternalBufferSize = AudioSystem.NOT_SPECIFIED;
        Getopt g = new Getopt("AudioPlayer", args, "hlufM:e:i:E:S:D");
        int c;
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'h':
                    printUsageAndExit();
                case 'l':
                    AudioCommon.listMixersAndExit(true);
                case 'u':
                    bInterpretFilenameAsUrl = true;
                    break;
                case 'f':
                    bInterpretFilenameAsUrl = false;
                    break;
                case 'M':
                    strMixerName = g.getOptarg();
                    if (DEBUG) out("AudioPlayer.main(): mixer name: " + strMixerName);
                    break;
                case 'e':
                    nExternalBufferSize = Integer.parseInt(g.getOptarg());
                    break;
                case 'i':
                    nInternalBufferSize = Integer.parseInt(g.getOptarg());
                    break;
                case 'E':
                    String strEndianess = g.getOptarg();
                    strEndianess = strEndianess.toLowerCase();
                    if (strEndianess.equals("big")) {
                        bBigEndian = true;
                    } else if (strEndianess.equals("little")) {
                        bBigEndian = false;
                    } else {
                        printUsageAndExit();
                    }
                    bForceConversion = true;
                    break;
                case 'S':
                    nSampleSizeInBits = Integer.parseInt(g.getOptarg());
                    bForceConversion = true;
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
        String strFilenameOrUrl = null;
        for (int i = g.getOptind(); i < args.length; i++) {
            if (strFilenameOrUrl == null) {
                strFilenameOrUrl = args[i];
            } else {
                printUsageAndExit();
            }
        }
        if (strFilenameOrUrl == null) {
            printUsageAndExit();
        }
        AudioInputStream audioInputStream = null;
        if (bInterpretFilenameAsUrl) {
            URL url = new URL(strFilenameOrUrl);
            audioInputStream = AudioSystem.getAudioInputStream(url);
        } else {
            if (strFilenameOrUrl.equals("-")) {
                InputStream inputStream = new BufferedInputStream(System.in);
                audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            } else {
                File file = new File(strFilenameOrUrl);
                audioInputStream = AudioSystem.getAudioInputStream(file);
            }
        }
        if (DEBUG) out("AudioPlayer.main(): primary AIS: " + audioInputStream);
        AudioFormat audioFormat = audioInputStream.getFormat();
        if (DEBUG) out("AudioPlayer.main(): primary format: " + audioFormat);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, nInternalBufferSize);
        boolean bIsSupportedDirectly = AudioSystem.isLineSupported(info);
        if (!bIsSupportedDirectly || bForceConversion) {
            AudioFormat sourceFormat = audioFormat;
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (nSampleSizeInBits / 8), sourceFormat.getSampleRate(), bBigEndian);
            if (DEBUG) {
                out("AudioPlayer.main(): source format: " + sourceFormat);
                out("AudioPlayer.main(): target format: " + targetFormat);
            }
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
            audioFormat = audioInputStream.getFormat();
            if (DEBUG) out("AudioPlayer.main(): converted AIS: " + audioInputStream);
            if (DEBUG) out("AudioPlayer.main(): converted format: " + audioFormat);
        }
        SourceDataLine line = getSourceDataLine(strMixerName, audioFormat, nInternalBufferSize);
        if (line == null) {
            out("AudioPlayer: cannot get SourceDataLine for format " + audioFormat);
            System.exit(1);
        }
        if (DEBUG) out("AudioPlayer.main(): line: " + line);
        if (DEBUG) out("AudioPlayer.main(): line format: " + line.getFormat());
        if (DEBUG) out("AudioPlayer.main(): line buffer size: " + line.getBufferSize());
        line.start();
        int nBytesRead = 0;
        byte[] abData = new byte[nExternalBufferSize];
        if (DEBUG) out("AudioPlayer.main(): starting main loop");
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (DEBUG) out("AudioPlayer.main(): read from AudioInputStream (bytes): " + nBytesRead);
            if (nBytesRead >= 0) {
                int nBytesWritten = line.write(abData, 0, nBytesRead);
                if (DEBUG) out("AudioPlayer.main(): written to SourceDataLine (bytes): " + nBytesWritten);
            }
        }
        if (DEBUG) out("AudioPlayer.main(): finished main loop");
        if (DEBUG) out("AudioPlayer.main(): before drain");
        line.drain();
        if (DEBUG) out("AudioPlayer.main(): before close");
        line.close();
    }
