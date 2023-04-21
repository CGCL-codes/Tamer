    private void testSaveAsWave() throws UnsupportedAudioFileException, IOException {
        Logger log = Logger.getLogger(ConsoleTest.class);
        log.debug("save as wave");
        log.debug("load audio");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("D:/Projects/Percussionist/data/instruments/djembe_1/bass.wav"));
        AudioFormat af = audioIn.getFormat();
        Map<String, Object> afProps = af.properties();
        System.out.println("properties");
        log.debug("Channels = " + af.getChannels());
        log.debug("SampleSizeInBits = " + af.getSampleSizeInBits());
        log.debug("SampleRate = " + af.getSampleRate());
        log.debug("FrameRate = " + af.getFrameRate());
        log.debug("FrameSize = " + af.getFrameSize());
        FloatSampleBuffer readBuffer = new FloatSampleBuffer();
        FloatSampleBuffer mixBuffer = new FloatSampleBuffer(af.getChannels(), 0, af.getSampleRate());
        mixBuffer.changeSampleCount((int) (1 * 60 * af.getFrameRate()), false);
        byte[] b = mixBuffer.convertToByteArray(af);
        System.out.println(b.length);
        System.out.println(mixBuffer.getAllChannels().length);
        log.debug("finished");
    }
