    public static Manifest lookupManifestByGA(String groupId, String artifactId) {
        try {
            final ClassLoader cl = BbxClassLoaderUtils.class.getClassLoader();
            final Enumeration<URL> resources = cl.getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                final URL url = resources.nextElement();
                final InputStream is = url.openStream();
                try {
                    final Manifest manifest = new Manifest(is);
                    final Attributes mainAttributes = manifest.getMainAttributes();
                    if (!groupId.equals(mainAttributes.get(Attributes.Name.IMPLEMENTATION_VENDOR_ID))) continue;
                    if (!artifactId.equals(mainAttributes.get(Attributes.Name.SPECIFICATION_TITLE))) continue;
                    return manifest;
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("Cannot find manifest with identification " + groupId + ":" + artifactId);
    }
