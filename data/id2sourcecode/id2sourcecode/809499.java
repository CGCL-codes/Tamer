    public static void main(final String[] args) throws IOException {
        if (args.length < 1 || args.length > 2) {
            System.err.println("usage: java PDFViewer pdfspec [pagenum]");
            return;
        }
        int pagenum = (args.length == 1) ? 1 : Integer.parseInt(args[1]);
        if (pagenum < 1) pagenum = 1;
        RandomAccessFile raf = new RandomAccessFile(new File(args[0]), "r");
        FileChannel fc = raf.getChannel();
        ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        PDFFile pdfFile = new PDFFile(buf);
        int numpages = pdfFile.getNumPages();
        System.out.println("Number of pages = " + numpages);
        if (pagenum > numpages) pagenum = numpages;
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

            public void run() {
                new PDFViewer("PDF Viewer: " + args[0]);
            }
        };
        EventQueue.invokeLater(r);
    }
