    private void getImages() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("jvmins.pdf", "r");
        FileChannel fc = raf.getChannel();
        ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        PDFFile pdfFile = new PDFFile(buf);
        images = new Image[entry.numPages];
        for (int i = 0; i < entry.numPages; i++) {
            PDFPage page = pdfFile.getPage(entry.startPage + i);
            Rectangle2D r2d = page.getBBox();
            r2d.setRect(r2d.getX() + 36, r2d.getY() + 36, r2d.getWidth() - 72, r2d.getHeight() - 72);
            Dimension dim = page.getUnstretchedSize(700, 700, r2d);
            images[i] = page.getImage(dim.width, dim.height, r2d, null, true, true);
        }
    }
