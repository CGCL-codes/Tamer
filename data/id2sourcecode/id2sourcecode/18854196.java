    private void loadClassesFromJar(URL url, Set<Class<?>> files) throws ClassNotFoundException {
        try {
            InputStream stream = url.openStream();
            JarInputStream fileNames = new JarInputStream(stream);
            JarEntry entry = null;
            while ((entry = fileNames.getNextJarEntry()) != null) {
                if (entry.getName().endsWith(".class")) {
                    add(files, entry.getName());
                }
            }
            fileNames.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
