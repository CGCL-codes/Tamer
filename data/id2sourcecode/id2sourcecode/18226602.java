    public void saveAs(final File file) throws IOException {
        final ImageReader reader = getImageReader();
        ImageIO.write(reader.read(0), reader.getFormatName(), file);
    }
