    private String getThumbnailForPdf(String path) throws IOException {
        try {
            RandomAccessFile raf = new RandomAccessFile(new File(path), "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = new PDFFile(buf);
            PDFPage page = pdffile.getPage(0, true);
            Rectangle2D bb = page.getBBox();
            int width = bb == null ? (int) page.getWidth() : (int) bb.getWidth();
            int height = bb == null ? (int) page.getHeight() : (int) bb.getHeight();
            Rectangle rect = new Rectangle(0, 0, width, height);
            BufferedImage img = (BufferedImage) page.getImage(rect.width, rect.height, rect, null, true, true);
            return super.saveThumbnail(img);
        } catch (Throwable e) {
            System.gc();
            return null;
        }
    }
