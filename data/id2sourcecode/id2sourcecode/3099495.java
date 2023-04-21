    public static javax.swing.ImageIcon iconJar(String _codebase, String _jarFile, String _gifFile, boolean _verbose) {
        if (_gifFile == null || _jarFile == null) {
            return null;
        }
        javax.swing.ImageIcon icon = null;
        java.io.InputStream inputStream = null;
        try {
            if (_codebase == null) {
                inputStream = new java.io.FileInputStream(_jarFile);
            } else {
                java.net.URL url = new java.net.URL(_codebase + _jarFile);
                inputStream = url.openStream();
            }
            java.util.jar.JarInputStream jis = new java.util.jar.JarInputStream(inputStream);
            boolean done = false;
            byte[] b = null;
            while (!done) {
                java.util.jar.JarEntry je = jis.getNextJarEntry();
                if (je == null) {
                    break;
                }
                if (je.isDirectory()) {
                    continue;
                }
                if (je.getName().equals(_gifFile)) {
                    long size = (int) je.getSize();
                    int rb = 0;
                    int chunk = 0;
                    while (chunk >= 0) {
                        chunk = jis.read(enormous, rb, 255);
                        if (chunk == -1) {
                            break;
                        }
                        rb += chunk;
                    }
                    size = rb;
                    b = new byte[(int) size];
                    System.arraycopy(enormous, 0, b, 0, (int) size);
                    done = true;
                }
            }
            icon = new javax.swing.ImageIcon(b);
        } catch (Exception exc) {
            if (_verbose) {
                exc.printStackTrace();
            }
            icon = null;
        }
        return icon;
    }
