    private void init(URL[] urls) throws IOException {
        for (URL url : urls) {
            if ("jar".equals(url.getProtocol())) {
                try {
                    checkJarForJolieExtensions((JarURLConnection) url.openConnection());
                } catch (IOException e) {
                    throw new IOException("Loading failed for jolie extension jar " + url.toString(), e);
                }
            }
        }
    }
