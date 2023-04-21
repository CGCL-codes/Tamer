    public static void main(String[] args) {
        if (args.length != 2) {
            printUsageAndExit();
        }
        File pcmFile = new File(args[0]);
        File gsmFile = new File(args[1]);
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(pcmFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ais == null) {
            out("cannot open audio file");
            System.exit(1);
        }
        AudioFormat sourceFormat = ais.getFormat();
        if (!sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) || sourceFormat.getSampleRate() != 8000.0F || sourceFormat.getSampleSizeInBits() != 16 || sourceFormat.getChannels() != 1) {
            out("The format of the input data has to be PCM 8 kHz 16 bit mono");
            System.exit(1);
        }
        AudioFormat.Encoding targetEncoding = new AudioFormat.Encoding("GSM0610");
        AudioInputStream gsmAIS = AudioSystem.getAudioInputStream(targetEncoding, ais);
        AudioFileFormat.Type fileType = new AudioFileFormat.Type("GSM", ".gsm");
        int nWrittenFrames = 0;
        try {
            nWrittenFrames = AudioSystem.write(gsmAIS, fileType, gsmFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
