    private static void decode(String arg) throws Exception {
        final SampleBuffer buf = new SampleBuffer();
        SourceDataLine line = null;
        byte[] b;
        try {
            final URI uri = new URI(arg);
            final Socket sock = new Socket(uri.getHost(), uri.getPort() > 0 ? uri.getPort() : 80);
            final PrintStream out = new PrintStream(sock.getOutputStream());
            String path = uri.getPath();
            if (path == null || path.equals("")) path = "/";
            if (uri.getQuery() != null) path += "?" + uri.getQuery();
            out.println("GET " + path + " HTTP/1.1");
            out.println("Host: " + uri.getHost());
            out.println();
            final DataInputStream in = new DataInputStream(sock.getInputStream());
            String x;
            do {
                x = in.readLine();
            } while (x != null && !x.trim().equals(""));
            final ADTSDemultiplexer adts = new ADTSDemultiplexer(in);
            AudioFormat aufmt = new AudioFormat(adts.getSampleFrequency(), 16, adts.getChannelCount(), true, true);
            final Decoder dec = new Decoder(adts.getDecoderSpecificInfo());
            while (true) {
                b = adts.readNextFrame();
                dec.decodeFrame(b, buf);
                if (line != null && !line.getFormat().matches(aufmt)) {
                    line.stop();
                    line.close();
                    line = null;
                    aufmt = new AudioFormat(buf.getSampleRate(), buf.getBitsPerSample(), buf.getChannels(), true, true);
                }
                if (line == null) {
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
