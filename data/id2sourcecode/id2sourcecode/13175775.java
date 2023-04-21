    private ImageReader findImageReader(URL url) {
        ImageInputStream input = null;
        try {
            input = ImageIO.createImageInputStream(url.openStream());
        } catch (IOException e) {
            logger.log(Level.WARNING, "bad image URL " + url, e);
        }
        ImageReader reader = null;
        if (input != null) {
            Iterator readers = ImageIO.getImageReaders(input);
            while ((reader == null) && (readers != null) && readers.hasNext()) {
                reader = (ImageReader) readers.next();
            }
            reader.setInput(input);
        }
        return reader;
    }
