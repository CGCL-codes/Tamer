    protected ResourceBundle createResourceBundle(Locale locale) {
        InputStream in = null;
        try {
            URL url = bundleContext.getBundle().getEntry(getResourceBundlePath(locale));
            if (url == null) return null;
            return new PropertyResourceBundle(url.openStream());
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug("Can't open resource " + getResourceBundlePathBase() + "_" + locale.toString() + ".properties");
            }
            return null;
        } finally {
            IoUtils.closeIO(in);
        }
    }
