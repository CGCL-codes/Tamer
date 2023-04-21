    private synchronized File extractToEnd() throws IOException {
        File f = File.createTempFile("tmp1", ".dat", new File(Root));
        RandomAccessFile oraf = new RandomAccessFile(f, "rw");
        oraf.getChannel().transferFrom(RAF.getChannel(), 0, RAF.length() - RAF.getFilePointer());
        oraf.close();
        return f;
    }
