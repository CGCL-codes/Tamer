    public void openFile(File file, String password) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer bb = ByteBuffer.NEW(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
        if (password == null) mPdfFile = new PDFFile(bb); else mPdfFile = new PDFFile(bb, new PDFPassword(password));
        mGraphView.showText("Anzahl Seiten:" + mPdfFile.getNumPages());
    }
