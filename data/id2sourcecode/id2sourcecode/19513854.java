    private static ByteBuffer addData() throws IOException {
        FileInputStream fis = new FileInputStream(datafile);
        FileChannel fc = fis.getChannel();
        ByteBuffer b = fc.map(FileChannel.MapMode.READ_ONLY, 0, maxOccurs * 6 * 4);
        return b;
    }
