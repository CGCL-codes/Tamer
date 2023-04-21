    public ZPDFPageImg(long x, long y, int z, String filePath, int pageNumber) throws IOException {
        this.filePath = filePath;
        this.pageNumber = pageNumber;
        File file = new File(filePath);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdfFile = new PDFFile(buf);
        PDFPage page = pdfFile.getPage(pageNumber);
        initZPDFPageImg(x, y, z, page);
    }
