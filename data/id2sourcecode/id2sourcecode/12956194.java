    public Renderer(String file, int pagenum) {
        fileName = file;
        try {
            RandomAccessFile raf = new RandomAccessFile(new File(fileName), "r");
            FileChannel fc = raf.getChannel();
            ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            pdfFile = new PDFFile(buf);
        } catch (IOException e) {
            System.out.println("the file wasn't found");
        }
        numpages = pdfFile.getNumPages();
        System.out.println("Number of pages = " + numpages);
        images = new Image[numpages];
        int counter = 1;
        for (int i = 0; i < images.length; i++) {
            PDFPage page = pdfFile.getPage(counter);
            Rectangle2D r2d = page.getBBox();
            width = r2d.getWidth();
            height = r2d.getHeight();
            width /= 72.0;
            height /= 72.0;
            int res = Toolkit.getDefaultToolkit().getScreenResolution();
            width *= res;
            height *= res;
            image = page.getImage((int) width, (int) height, r2d, null, true, true);
            images[i] = image;
            counter++;
        }
    }
