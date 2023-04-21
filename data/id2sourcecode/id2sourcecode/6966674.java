    private void loadMappedBinary(FileInputStream is) throws IOException {
        FileChannel fc = is.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
        bb.load();
        loadDatabase(bb);
        is.close();
    }
