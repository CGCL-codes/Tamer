    private void readFromFile(File file) throws IOException {
        _fis = new FileInputStream(file);
        _chan = _fis.getChannel();
        ByteBuffer buf = _chan.map(FileChannel.MapMode.READ_ONLY, 0, (int) file.length());
        readFromBuffer(buf);
    }
