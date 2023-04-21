    public static boolean fileExistsInJar(String _codebase, String _jarFile, String _filename) {
        if (_filename == null || _jarFile == null) {
            return false;
        }
        java.io.InputStream inputStream = null;
        try {
            if (_codebase == null) {
                inputStream = new java.io.FileInputStream(_jarFile);
            } else {
                java.net.URL url = new java.net.URL(_codebase + _jarFile);
                inputStream = url.openStream();
            }
            java.util.jar.JarInputStream jis = new java.util.jar.JarInputStream(inputStream);
            while (true) {
                java.util.jar.JarEntry je = jis.getNextJarEntry();
                if (je == null) {
                    break;
                }
                if (je.isDirectory()) {
                    continue;
                }
                if (je.getName().equals(_filename)) {
                    return true;
                }
            }
        } catch (Exception exc) {
            return false;
        }
        return false;
    }
