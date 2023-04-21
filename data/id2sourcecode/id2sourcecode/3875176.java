    private InputStream peekInsideJar(String jar, String resourcePath) {
        String resourceURL = jar + "!" + resourcePath;
        try {
            URL url = new URL(resourceURL);
            InputStream in = url.openStream();
            if (in != null) return in;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        try {
            String zipPath = jar.replaceFirst("^jar:file:", "");
            String zipResourcePath = resourcePath.replaceFirst("^/", "");
            ZipFile zipFile = new ZipFile(zipPath);
            ZipEntry entry = zipFile.getEntry(zipResourcePath);
            if (entry != null) {
                return zipFile.getInputStream(entry);
            }
        } catch (java.io.IOException e) {
        }
        return null;
    }
