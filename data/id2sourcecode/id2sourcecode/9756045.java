    private PDFFile getPdfRendererPDF() throws IOException {
        if (pdf_renderer_pdf == null) {
            RandomAccessFile raf = new RandomAccessFile(pdf_file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            pdf_renderer_pdf = new PDFFile(buf);
        }
        return pdf_renderer_pdf;
    }
