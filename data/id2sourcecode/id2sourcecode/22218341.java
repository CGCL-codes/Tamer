    public FileMap(String file_name, FileChannel.MapMode mode, int RECORD_SIZE) throws IOException {
        assert (file_name != null && mode != null && RECORD_SIZE > 0) : "PRE-CONDIZIONE VIOLATA!";
        try {
            this.file_name = file_name;
            this.raf = new RandomAccessFile(file_name, "rw");
            this.file_channel = raf.getChannel();
            this.file_size = file_channel.size();
            this.mode = mode;
            this.buffer = file_channel.map(mode, 0, file_size);
            this.RECORD_SIZE = RECORD_SIZE;
            this.num_records = file_size / RECORD_SIZE;
            this.data = new byte[RECORD_SIZE];
        } catch (FileNotFoundException fnfe) {
            throw new IOException("File '" + file_name + "' not found!");
        }
        assert (INV()) : "INVARIANTE VIOLATA!";
    }
