    public List<ImageInformation> convert(final IFolder outputFolder, final IFile pdfFile, final IProgressMonitor monitor) {
        File file = pdfFile.getLocation().toFile();
        List<ImageInformation> returnValue = new ArrayList<ImageInformation>();
        PDFFile pdffile;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            pdffile = new PDFFile(buf);
            for (int i = 1, n = pdffile.getNumPages(); i <= n; i++) {
                PDFPage page = pdffile.getPage(i);
                Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
                Image img = page.getImage(rect.width, rect.height, rect, null, true, true);
                BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.createGraphics();
                g.drawImage(img, 0, 0, null);
                try {
                    String name = "__" + i + ".png";
                    ImageIO.write(bi, "png", outputFolder.getFile(name).getLocation().toFile());
                    ImageInformation item = new ImageInformation();
                    item.setFileName(name);
                    item.setHeight(rect.height);
                    item.setWidth(rect.width);
                    returnValue.add(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return returnValue;
    }
