    public FlatMap(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel channel = in.getChannel();
        bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
        bbuf.load();
        byte[] magic = new byte[MAGIC.length];
        magic = get(0, magic.length);
        size = toInt(get(4, 4));
        key_type = toInt(get(8, 4));
        value_type = toInt(get(12, 4));
        key_type_handler = lookupTypeHandler(key_type);
        value_type_handler = lookupTypeHandler(value_type);
    }
