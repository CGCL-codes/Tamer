    private FileChannel getFileToChannel(long num) throws IOException {
        this.f = new File(dir.getPath() + "/" + this.idf + this.ptrstr + "_" + num);
        FileChannel fc = new RandomAccessFile(f, "rw").getChannel();
        fc.force(true);
        return fc;
    }
