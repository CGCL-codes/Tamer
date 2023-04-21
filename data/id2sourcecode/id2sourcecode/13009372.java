    public FileChannel getFileChannel(boolean writeable) throws FileNotFoundException {
        if (file != null) return new RandomAccessFile(file, writeable ? "rw" : "r").getChannel();
        throw new FileNotFoundException();
    }
