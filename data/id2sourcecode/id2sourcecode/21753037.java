    public DBCFile<? extends DBCRecord> initialize(String pathAndFilename) throws IOException {
        if (this.data == null) {
            File f = new File(pathAndFilename);
            FileInputStream fis = new FileInputStream(f);
            FileChannel fc = fis.getChannel();
            this.data = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            this.data.order(ByteOrder.LITTLE_ENDIAN);
            read(this.data);
        }
        return this;
    }
