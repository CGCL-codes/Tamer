    private void readFromFile(File file) throws IOException {
        fis = new FileInputStream(file);
        chan = fis.getChannel();
        ByteBuffer buf = chan.map(FileChannel.MapMode.READ_ONLY, 0, (int) file.length());
        readFromBuffer(buf);
    }
