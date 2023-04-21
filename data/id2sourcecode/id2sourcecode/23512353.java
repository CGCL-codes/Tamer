    void load(String filePath, float detailFactor) {
        try {
            File f = new File(filePath);
            RandomAccessFile raf = new RandomAccessFile(f, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdfFile = new PDFFile(buf);
            int page_width = (int) pdfFile.getPage(0).getBBox().getWidth();
            for (int i = 0; i < pdfFile.getNumPages(); i++) {
                try {
                    vs.addGlyph(new ZPDFPageImg(i * Math.round(page_width * 1.1f * detailFactor), i * Math.round(page_width * 1.1f * detailFactor), 0, filePath, i + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
