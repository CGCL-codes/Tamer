    public void updateBundleFromURL(long bundleIdentifier, String url) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            try {
                bundle.update(new URL(url).openStream());
            } catch (Exception e) {
                throw new IOException("Unable to update bundle: " + bundleIdentifier);
            }
        } catch (IllegalArgumentException e) {
            logVisitor.warning("updateBundleFromURL error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("updateBundleFromURL error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("updateBundleFromURL error", e);
            throw new IOException(e.getMessage());
        }
    }
