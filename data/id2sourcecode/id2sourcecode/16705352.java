    public PDFPage openFile(File file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = null;
        try {
            pdffile = new PDFFile(buf);
            PDFPage page = pdffile.getPage(0);
            System.err.println(page.getBBox());
            return page;
        } catch (IOException ioe) {
            return null;
        }
    }
