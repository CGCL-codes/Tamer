    public static void write(final File location, final ExifRawMetadata metadata) throws IOException {
        Validator.notNull(location, "Image location");
        Validator.notNull(metadata, "Metadata");
        TiffOutputSet outputSet = metadata.getFinalOutputSet();
        try {
            final String fileName = location.getName();
            final File tempLocation = File.createTempFile("pagger-" + System.currentTimeMillis(), "." + fileName.substring(fileName.lastIndexOf(".") + 1));
            OutputStream out = null;
            try {
                final ExifRewriter writer = new ExifRewriter();
                out = new FileOutputStream(tempLocation);
                writer.updateExifMetadataLossy(location, out, outputSet);
            } finally {
                IOUtils.closeQuietly(out);
            }
            FileUtils.copyFile(tempLocation, location);
        } catch (ImageReadException ex) {
            throw new IOException(ex);
        } catch (ImageWriteException ex) {
            throw new IOException(ex);
        }
    }
