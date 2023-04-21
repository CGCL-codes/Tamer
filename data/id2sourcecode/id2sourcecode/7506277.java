    protected void resolveDirectory(File file, List<BundleDefinition> bundles) {
        logger.debug("Resolve directory - " + file.getAbsolutePath());
        InputStream in = null;
        try {
            URL fileUrl = file.toURI().toURL();
            URL url = new URL(fileUrl + "/" + OSGI_BUNDLE_MANIFEST);
            in = url.openStream();
            Headers headers = Headers.parseManifest(in);
            addBundleDefinition(bundles, fileUrl.toString(), headers);
        } catch (Exception e) {
            logger.debug("Unable to resolve [" + file.getAbsolutePath() + "] - " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }
