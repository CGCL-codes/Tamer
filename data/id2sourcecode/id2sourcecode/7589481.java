    private Dimension getImageSize(URL url) throws IOException {
        ImageInputStream in = ImageIO.createImageInputStream(url.openStream());
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (!readers.hasNext()) return null;
            ImageReader reader = readers.next();
            reader.setInput(in);
            return new Dimension(reader.getWidth(0), reader.getHeight(0));
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) in.close();
        }
    }
