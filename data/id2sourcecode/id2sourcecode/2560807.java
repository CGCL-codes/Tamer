    public static InputStream getInputStream(final URL url, final String name) throws URISyntaxException, IOException {
        String urlString = url.toString();
        boolean isJar = urlString.startsWith(ClassLoaderUtils.JAR_PREFIX);
        boolean isVSFFile = urlString.startsWith(ClassLoaderUtils.VFSFILE_PREFIX);
        boolean isVSFZip = urlString.startsWith(ClassLoaderUtils.VFSZIP_PREFIX);
        if ((isVSFFile) || (isVSFZip)) {
            InputStream input = url.openStream();
            return input;
        }
        if (isJar) {
            URI uri = ClassLoaderUtils.getURI(url);
            JarFile file = new JarFile(new File(uri));
            JarEntry entry = file.getJarEntry(name);
            InputStream input = file.getInputStream(entry);
            return input;
        }
        InputStream input = url.openStream();
        return input;
    }
