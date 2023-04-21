    public void print() {
        try {
            RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = new PDFFile(buf);
            NonScalingPdfPrintPage pages = new NonScalingPdfPrintPage(pdffile);
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
            Paper paper = new Paper();
            PDFPage page = pdffile.getPage(0);
            paper.setSize(page.getWidth(), page.getHeight());
            paper.setImageableArea(0, 0, page.getWidth(), page.getHeight());
            pageFormat.setPaper(paper);
            printerJob.setJobName(pdfFile.getName());
            Book book = new Book();
            book.append(pages, pageFormat, 1);
            printerJob.setPageable(book);
            printerJob.print();
        } catch (Exception e) {
            throw new PdfRenderingException(e);
        }
    }
