    public static void extractBundleEntry(final String bundleSymbolicName, final String pathWithinBundle, final File destination) throws IOException {
        InputStream is = null;
        OutputStream fileOutStream = null;
        try {
            final Bundle bundle = Platform.getBundle(bundleSymbolicName);
            if (bundle == null) {
                throw new IOException("Bundle " + bundleSymbolicName + " not found.");
            }
            final URL url = bundle.getEntry(pathWithinBundle);
            if (url == null) {
                throw new IOException("Bundle entry " + pathWithinBundle + " not found.");
            }
            is = url.openStream();
            final File destParentDir = destination.getParentFile();
            if (destParentDir != null) {
                destParentDir.mkdirs();
            }
            fileOutStream = new BufferedOutputStream(new FileOutputStream(destination));
            final byte[] copyBuffer = new byte[10240];
            int bytesRead;
            while ((bytesRead = is.read(copyBuffer)) != -1) {
                fileOutStream.write(copyBuffer, 0, bytesRead);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
                }
            }
            if (fileOutStream != null) {
                try {
                    fileOutStream.close();
                } catch (final IOException e) {
                }
            }
        }
    }
