    public static String readVersion(Class<?> clazz) throws DagoException {
        InputStream manifestStream = null;
        try {
            URL urlManifest = new URL(getPathToManifest(clazz));
            manifestStream = urlManifest.openStream();
            Manifest manifest = new Manifest(manifestStream);
            return manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
        } catch (Exception err) {
            throw new DagoException(I18NMessages.failToReadVersion, err);
        } finally {
            if (manifestStream != null) {
                try {
                    manifestStream.close();
                } catch (IOException err) {
                }
            }
        }
    }
