    public MappedReader(File f) throws Exception {
        fc = new FileInputStream(f).getChannel();
        mbb = fc.map(MapMode.READ_ONLY, 0, fc.size());
        mbb.order(ByteOrder.LITTLE_ENDIAN);
    }
