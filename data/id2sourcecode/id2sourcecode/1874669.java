    private static void decodeAAC(String in) throws Exception {
        SourceDataLine line = null;
        byte[] b;
        try {
            final ADTSDemultiplexer adts = new ADTSDemultiplexer(new FileInputStream(in));
            final Decoder dec = new Decoder(adts.getDecoderSpecificInfo());
            final SampleBuffer buf = new SampleBuffer();
            while (true) {
                b = adts.readNextFrame();
                dec.decodeFrame(b, buf);
                if (line == null) {
                    final AudioFormat aufmt = new AudioFormat(buf.getSampleRate(), buf.getBitsPerSample(), buf.getChannels(), true, true);
                    line = AudioSystem.getSourceDataLine(aufmt);
                    line.open();
                    line.start();
                }
                b = buf.getData();
                line.write(b, 0, b.length);
            }
        } finally {
            if (line != null) {
                line.stop();
                line.close();
            }
        }
    }
