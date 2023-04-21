    public static BufferedImage readDicom(final SourceImage src, final URL url) {
        assert url != null;
        assert src != null;
        BufferedImage bi = null;
        try {
            DicomInputStream dis = new DicomInputStream(new BufferedInputStream(url.openStream()));
            src.read(dis);
            dis.close();
            bi = src.getBufferedImage();
        } catch (Exception exc) {
            System.out.println("ImageFactory::readDicom(): exc=" + exc);
        }
        return bi;
    }
