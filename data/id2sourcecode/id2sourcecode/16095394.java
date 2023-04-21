        public FileStreamer(IDataHandler orgDataHandler, int length, String filename) throws IOException {
            this.orgDataHandler = orgDataHandler;
            this.remaining = length;
            this.filename = filename;
            raf = new RandomAccessFile(filename, "rw");
            fc = raf.getChannel();
        }
