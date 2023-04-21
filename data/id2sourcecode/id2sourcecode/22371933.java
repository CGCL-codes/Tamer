    private synchronized CharStream getURLStream(String resourcename) throws IOException {
        resourcename = "dict/" + resourcename;
        final URL url = getClass().getClassLoader().getResource(resourcename);
        if (url == null) {
            log.debug("resourcename: {} not found in classpath", resourcename);
            return null;
        }
        final URLConnection conn = url.openConnection();
        final int len;
        if (conn instanceof JarURLConnection) {
            final JarURLConnection juc = (JarURLConnection) conn;
            len = (int) juc.getJarEntry().getSize();
        } else {
            len = conn.getContentLength();
        }
        final InputStream input = conn.getInputStream();
        return new InputStreamCharStream(resourcename, input, len);
    }
