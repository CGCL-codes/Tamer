    private void loadBench(String benchLocation, Map<String, Map<String, String>> bench) throws Exception {
        File testConf = new File(benchLocation + "/default.conf");
        if (testConf.canRead()) {
            FileInputStream fis = new FileInputStream(testConf);
            FileChannel fc = fis.getChannel();
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            Charset charset = Charset.forName("ISO-8859-15");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer cb = decoder.decode(bb);
            loadBench(testConf, cb, bench);
            fc.close();
        }
    }
