    private static PXStringBundle loadBundle(PXResourceLocation location_) {
        PXStringBundle loadBundle = null;
        LOG.debug(location_);
        URL url = location_.getUrl();
        LOG.debug("url: " + url);
        if (url != null) {
            InputStream bundleStream = null;
            try {
                bundleStream = url.openStream();
                LOG.debug("bundleStream: " + bundleStream);
                if (bundleStream != null) {
                    PXXmlProperties bundleProperties = new PXXmlProperties();
                    bundleProperties.loadFromXML(bundleStream);
                    loadBundle = new PXStringBundle(bundleProperties, location_.getLocale());
                }
            } catch (Exception e) {
                LOG.error(null, e);
            } finally {
                PXStreamUtility.close(bundleStream);
            }
        }
        return loadBundle;
    }
