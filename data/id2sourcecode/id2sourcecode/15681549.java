    private synchronized void attachToEnd(File f) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        RAF.getChannel().transferFrom(raf.getChannel(), RAF.getFilePointer(), raf.length());
        raf.close();
    }
