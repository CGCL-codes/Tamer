    private boolean isJarInputStream(URL url) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            if (in == null) {
                return false;
            }
            JarInputStream jis = new JarInputStream(in);
            jis.close();
            return true;
        } catch (IOException ioe) {
            if (in != null) {
                in.close();
            }
            return false;
        }
    }
