    public static Manifest lookupManifestByMainClass(String mainClass) {
        try {
            final ClassLoader cl = BbxClassLoaderUtils.class.getClassLoader();
            final Enumeration<URL> resources = cl.getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                final URL url = resources.nextElement();
                final InputStream is = url.openStream();
                try {
                    final Manifest manifest = new Manifest(is);
                    if (!mainClass.equals(manifest.getMainAttributes().get(Attributes.Name.MAIN_CLASS))) continue;
                    return manifest;
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("Cannot find manifest with Main-Class = " + mainClass);
    }
