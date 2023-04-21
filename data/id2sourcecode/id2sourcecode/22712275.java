    public void openFile(File file) throws IOException, InterruptedException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdfFile = new PDFFile(buf);
        int page = PAGENUMBER;
        int numPages = pdfFile.getNumPages();
        log("Seite: " + page + "/" + numPages);
        PDFPage pdfPage = pdfFile.getPage(page);
        log("\n" + pdfPage.getPageNumber() + ":" + pdfPage.getWidth() + "x" + pdfPage.getHeight());
        float zoom = 1.00f;
        final Image pdfPageImg = pdfPage.getImage((int) (zoom * pdfPage.getWidth()), (int) (zoom * pdfPage.getHeight()), null, null, true, true);
        BufferedImage pdfBI = (BufferedImage) pdfPageImg;
        JFrame frame = new JFrame("AWT PDF Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyJPanel panel = new MyJPanel();
        panel.setImage(pdfBI);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setSize(maxX, maxY);
        frame.setVisible(true);
    }
