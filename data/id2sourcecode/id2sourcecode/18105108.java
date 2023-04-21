    public void openFile(File file) throws RenderException {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            this.pdfFile = new PDFFile(buf);
        } catch (IOException ioe) {
            throw new RenderException(ioe);
        }
    }
