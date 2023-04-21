    private void testFormat(String format) {
        ImageReader reader = ImageIO.getImageReadersByFormatName(format).next();
        ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
        assertEquals("getImageReader() returns an incorrect reader for " + format, ImageIO.getImageReader(writer).getClass(), reader.getClass());
        assertEquals("getImageWriter() returns an incorrect writer for " + format, ImageIO.getImageWriter(reader).getClass(), writer.getClass());
    }
