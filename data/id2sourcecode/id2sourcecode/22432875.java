    private void doSplitChannelReader(Fun2<Integer, ByteBuffer, Iterable<InputStream>> blockReader, Fun2<Fun1<InputStream, Map<String, Integer>>, Iterable<InputStream>, Iterable<Map<String, Integer>>> fun) throws Exception {
        File f = new File(FNAME);
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
        try {
            Iterable<InputStream> streams = blockReader.apply((int) (fc.size() / Context.MaxPool), bb);
            HashMap<String, Integer> results = foldl(new Fun2<Map<String, Integer>, HashMap<String, Integer>, HashMap<String, Integer>>() {

                @Override
                public HashMap<String, Integer> apply(Map<String, Integer> p1, HashMap<String, Integer> p2) {
                    for (Map.Entry<String, Integer> entry : p1.entrySet()) {
                        int i = entry.getValue();
                        int j = (p2.containsKey(entry.getKey())) ? p2.get(entry.getKey()) : 0;
                        p2.put(entry.getKey(), i + j);
                    }
                    return p2;
                }
            }, fun.apply(Fun3.compose(new Text.InputStreamToBufferedReader().apply(Charset.forName("us-ascii")), new Unmemoize.Unmemoizer1<BufferedReader, String>().apply(Text.readLines), new Ranking().apply(pattern)), streams), new HashMap<String, Integer>());
            new Printem().apply(results);
        } finally {
            fc.close();
        }
    }
