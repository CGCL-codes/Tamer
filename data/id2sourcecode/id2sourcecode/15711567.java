    public static void main(String[] args) throws Exception {
        String usage = "Usage: MapFile inFile outFile";
        if (args.length != 2) {
            System.err.println(usage);
            System.exit(-1);
        }
        String in = args[0];
        String out = args[1];
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.getLocal(conf);
        MapFile.Reader reader = new MapFile.Reader(fs, in, conf);
        MapFile.Writer writer = new MapFile.Writer(conf, fs, out, reader.getKeyClass().asSubclass(WritableComparable.class), reader.getValueClass());
        WritableComparable key = ReflectionUtils.newInstance(reader.getKeyClass().asSubclass(WritableComparable.class), conf);
        Writable value = ReflectionUtils.newInstance(reader.getValueClass().asSubclass(Writable.class), conf);
        while (reader.next(key, value)) writer.append(key, value);
        writer.close();
    }
