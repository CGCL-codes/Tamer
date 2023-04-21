    public void testByteChannelReader_fileChannelSource() throws IOException {
        File file = createFile();
        FileChannel inChannel = new FileInputStream(file).getChannel();
        BufferedXReader in = new BufferedXReader(new ByteChannelReader(inChannel, Charset.forName("ISO-8859-1").newDecoder()));
        long ops = 0;
        PerformanceMeter meter = initBenchmark();
        gc();
        meter.start();
        do {
            while (in.read() >= 0) ;
            ops += BYTE_COUNT;
            inChannel.position(0);
        } while (meter.getRunTime() < 5000);
        meter.stop(ops);
        in.close();
    }
