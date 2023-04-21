    private PDFPage getPdfPage() {
        try {
            RandomAccessFile raf = new RandomAccessFile(sourceFile, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdfFile = new PDFFile(buf);
            PDFPage pdfPage = pdfFile.getPage(getPageNum() + 1, true);
            return pdfPage;
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
