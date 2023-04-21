    public static void write(final File location, final IptcRawMetadata metadata) throws IOException {
        Validator.notNull(location, "Image location");
        Validator.notNull(metadata, "Metadata");
        PhotoshopApp13Data outputSet = metadata.getFinalOutputSet();
        try {
            final String fileName = location.getName();
            final File tempLocation = File.createTempFile("pagger-" + System.currentTimeMillis(), "." + fileName.substring(fileName.lastIndexOf(".") + 1));
            OutputStream out = null;
            try {
                final JpegIptcRewriter writer = new JpegIptcRewriter();
                out = new FileOutputStream(tempLocation);
                writer.writeIPTC(location, out, outputSet);
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
