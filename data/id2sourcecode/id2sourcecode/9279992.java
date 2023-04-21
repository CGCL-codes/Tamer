        public UploadHandler(int totalSize, boolean writeToFile) throws IOException {
            this.writeToFile = writeToFile;
            file = File.createTempFile("testfile", "tmp");
            fc = new RandomAccessFile(file, "rw").getChannel();
            this.totalSize = totalSize;
        }
