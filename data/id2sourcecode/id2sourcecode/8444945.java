    private static String getAttributeValue(final String attributeId, final String defaultValue) {
        URL url;
        try {
            url = new URL("jar:" + VersionInfo.class.getResource(VersionInfo.class.getSimpleName() + ".class").getPath().split("!")[0] + "!/");
            JarURLConnection jarConnection;
            jarConnection = (JarURLConnection) url.openConnection();
            final Manifest manifest = jarConnection.getManifest();
            final Attributes attributes = manifest.getMainAttributes();
            final String value = attributes.getValue(attributeId);
            if (value != null) {
                return value;
            }
        } catch (final MalformedURLException e) {
            return defaultValue;
        } catch (final IOException e) {
            return defaultValue;
        }
        return defaultValue;
    }
