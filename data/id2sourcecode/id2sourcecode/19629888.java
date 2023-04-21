    private boolean createOutputFile(final File outFile, final String resPath) {
        try {
            final ResourcesAnchor anchor = ResourcesAnchor.getInstance();
            final URL url = anchor.getResource(resPath);
            final InputStream in = url.openStream();
            try {
                final long cpyBytes = IOCopier.copyToFile(in, outFile);
                if (cpyBytes < 0L) throw new StreamCorruptedException("Error (" + cpyBytes + ") on copy " + resPath + " contents");
            } finally {
                in.close();
            }
            debug("Created " + outFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            error("Failed (" + e.getClass().getSimpleName() + ")" + " to create " + outFile.getAbsolutePath() + ": " + e.getMessage());
            return false;
        }
    }
