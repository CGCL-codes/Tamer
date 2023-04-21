    public void testInputStreamXReader_fileInputStreamSource() throws IOException {
        File file = createFile();
        FileInputStream fileIn = new FileInputStream(file);
        fileIn.mark(Integer.MAX_VALUE);
        BufferedXReader in = new BufferedXReader(new InputStreamReader(fileIn, Charset.forName("ISO-8859-1").newDecoder()));
        long ops = 0;
        PerformanceMeter meter = initBenchmark();
        gc();
        meter.start();
        do {
            while (in.read() >= 0) ;
            ops += BYTE_COUNT;
            fileIn.getChannel().position(0);
        } while (meter.getRunTime() < 5000);
        meter.stop(ops);
        in.close();
    }
