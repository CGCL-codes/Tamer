    public void open(File f) throws Exception {
        FileInputStream fis = new FileInputStream(f);
        fc = fis.getChannel();
        int sz = (int) fc.size();
        mappedbuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
    }
