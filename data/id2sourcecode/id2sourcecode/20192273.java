    public long installBundleFromURL(String location, String url) throws IOException {
        try {
            Bundle bundle = visitor.installBundle(location, new URL(url).openStream());
            return bundle.getBundleId();
        } catch (Exception e) {
            logVisitor.warning("installBundleFromURL error", e);
            throw new IOException(e.getMessage());
        }
    }
