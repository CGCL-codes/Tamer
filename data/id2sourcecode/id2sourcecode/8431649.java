    private Class<?> loadClass(StickyLibrary j, String name) {
        URL url = embedder.getClass().getResource("/" + j.getJarPath());
        try {
            JarInputStream jar = new JarInputStream(url.openStream());
            return loadClass(jar, name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
