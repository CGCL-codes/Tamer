    public void manifest() {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Enumeration urls = classLoader.getResources("META-INF/MANIFEST.MF");
            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();
                InputStream in = url.openConnection().getInputStream();
                manifest(in, classLoader);
            }
        } catch (Exception ex) {
            throw new MappingException(ex);
        }
    }
