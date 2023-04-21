    private void mapDatabase(FileInputStream is) throws IOException {
        FileChannel fc = is.getChannel();
        mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
        mbb.load();
        loadDatabaseHeader(mbb);
    }
