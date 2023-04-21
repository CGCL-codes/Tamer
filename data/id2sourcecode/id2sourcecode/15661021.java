    public Dimension firstSlid(final IFile pdfFile) {
        File file = pdfFile.getLocation().toFile();
        List<ImageInformation> returnValue = new ArrayList<ImageInformation>();
        PDFFile pdffile;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            pdffile = new PDFFile(buf);
            if (pdffile.getNumPages() == 0) {
                return new Dimension(0, 0);
            }
            PDFPage page = pdffile.getPage(0);
            Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
            return new Dimension(rect.width, rect.height);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return new Dimension(0, 0);
    }
