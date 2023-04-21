    protected void loadPDF(File file) {
        RandomAccessFile raf = null;
        MappedByteBuffer buf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            pdffile = new PDFFile(buf);
            fileChanged = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
