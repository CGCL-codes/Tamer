    public static Manifest getManifest(Class<?> clazz) {
        URL url = clazz.getResource("/META-INF/MANIFEST.MF");
        if (url == null) return null;
        InputStream in = null;
        try {
            in = url.openStream();
            Manifest manifest = new Manifest(in);
            in.close();
            return manifest;
        } catch (IOException e) {
            throw new IllegalStateException("Bad manifest: " + clazz, e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
