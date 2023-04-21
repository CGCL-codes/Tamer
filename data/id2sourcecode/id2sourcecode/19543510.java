    public List<ResultSet> exec() throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        int size = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);
        CharBuffer cb = decoder.decode(bb);
        grep(cb);
        return results;
    }
