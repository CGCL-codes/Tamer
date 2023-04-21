    public void copyFileIntoReport(final URL source, final String destinationFilename) {
        if (source == null) {
            throw new NullPointerException("Source was null");
        }
        try {
            final InputStream stream = source.openStream();
            FileUtils.copyFile(stream, new File(this.rootDirectory + destinationFilename));
            stream.close();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
