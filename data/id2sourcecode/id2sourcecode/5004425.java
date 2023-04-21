    protected boolean isJar(URL url, byte[] buffer) {
        InputStream is = null;
        try {
            is = url.openStream();
            is.read(buffer, 0, JAR_MAGIC.length);
            if (Arrays.equals(buffer, JAR_MAGIC)) {
                log.debug("Found JAR: " + url);
                return true;
            }
        } catch (Exception e) {
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
        return false;
    }
