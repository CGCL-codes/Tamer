    public static void write(final File location, final XmpRawMetadata metadata) throws IOException {
        Validator.notNull(location, "Image location");
        Validator.notNull(metadata, "Metadata");
        try {
            final String fileName = location.getName();
            final File tempLocation = File.createTempFile("pagger-" + System.currentTimeMillis(), "." + fileName.substring(fileName.lastIndexOf(".") + 1));
            OutputStream out = null;
            try {
                String xmpString = transform(metadata);
                final JpegXmpRewriter writer = new JpegXmpRewriter();
                out = new FileOutputStream(tempLocation);
                writer.updateXmpXml(location, out, xmpString);
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
