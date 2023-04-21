    protected void resolveDirectory(File file, List<BundleDescription> bundles) {
        logger.debug("Resolve directory - " + file.getAbsolutePath());
        InputStream in = null;
        try {
            URL fileUrl = file.toURI().toURL();
            URL url = new URL(fileUrl + "/" + OSGI_BUNDLE_MANIFEST);
            in = url.openStream();
            Headers headers = Headers.parseManifest(in);
            BundleDescription description = stateObjectFactory.createBundleDescription(null, headers, fileUrl.toString(), bundleId--);
            bundles.add(description);
        } catch (Exception e) {
            logger.info("Unable to resolve [" + file.getAbsolutePath() + "] - " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }
