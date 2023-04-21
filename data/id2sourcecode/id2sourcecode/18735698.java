    public byte[] extract() throws IOException {
        this.extracted = false;
        this.preview = null;
        File file = new File(this.filename);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
        PDFPage page = pdffile.getPage(this.page - 1);
        Rectangle r = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
        this.width = r.width;
        this.height = r.height;
        Image img = page.getImage(this.width, this.height, r, null, true, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOException throwEx = null;
        try {
            ImageIO.write((RenderedImage) img, "png", baos);
            baos.flush();
            this.preview = baos.toByteArray();
            this.extracted = true;
        } catch (IOException ex) {
            this.preview = null;
            this.extracted = false;
            throwEx = ex;
        } finally {
            baos.close();
            if (throwEx != null) {
                throw throwEx;
            }
        }
        return this.preview;
    }
