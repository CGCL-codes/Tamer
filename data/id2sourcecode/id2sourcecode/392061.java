    public static void main(final String[] args) throws Exception {
        int pagenum = 1;
        System.out.println(ClassLoader.getSystemResource("test.pdf").toURI().toString());
        File file = new File(ClassLoader.getSystemResource("test.pdf").toURI());
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel fc = raf.getChannel();
        ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        PDFFile pdfFile = new PDFFile(buf);
        int numpages = pdfFile.getNumPages();
        System.out.println("Number of pages = " + numpages);
        if (pagenum > numpages) {
            pagenum = numpages;
        }
        PDFPage page = pdfFile.getPage(pagenum);
        Rectangle2D r2d = page.getBBox();
        double width = r2d.getWidth();
        double height = r2d.getHeight();
        width /= 72.0;
        height /= 72.0;
        int res = Toolkit.getDefaultToolkit().getScreenResolution();
        width *= res;
        height *= res;
        image = page.getImage((int) width, (int) height, r2d, null, true, true);
        Runnable r = new Runnable() {

            @Override
            public void run() {
                new Main5("PDF Viewer: ");
            }
        };
        EventQueue.invokeLater(r);
    }
