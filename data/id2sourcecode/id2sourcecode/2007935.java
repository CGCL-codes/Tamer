    private static void decodeAAC(String in, String out) throws IOException {
        WaveFileWriter wav = null;
        try {
            final ADTSDemultiplexer adts = new ADTSDemultiplexer(new FileInputStream(in));
            final Decoder dec = new Decoder(adts.getDecoderSpecificInfo());
            final SampleBuffer buf = new SampleBuffer();
            byte[] b;
            while (true) {
                b = adts.readNextFrame();
                dec.decodeFrame(b, buf);
                if (wav == null) wav = new WaveFileWriter(new File(out), buf.getSampleRate(), buf.getChannels(), buf.getBitsPerSample());
                wav.write(buf.getData());
            }
        } finally {
            if (wav != null) wav.close();
        }
    }
