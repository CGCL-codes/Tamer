    public BufferedImage getImageUsingPdfRenderer(final int aPage) {
        BufferedImage tmpResult = null;
        try {
            File file = new File(options.getInFile());
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = null;
            try {
                pdffile = new PDFFile(buf);
            } catch (PDFParseException ppe) {
                try {
                    pdffile = new PDFFile(buf, new PDFPassword(""));
                } catch (PDFParseException ppe2) {
                    pdffile = new PDFFile(buf, new PDFPassword(options.getPdfOwnerPwdStr()));
                }
            }
            PDFPage page = pdffile.getPage(aPage);
            Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
            tmpResult = (BufferedImage) page.getImage(rect.width, rect.height, rect, null, true, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpResult;
    }
