    private static final synchronized String copyResourceToLocalFilesystem(ClassLoader classloader, String resourcePath) throws IOException {
        File resourceFile = new File(LOCAL_DIRECTORY_PATH + resourcePath);
        File parentFile = resourceFile.getParentFile();
        if (parentFile == null) {
            return null;
        }
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        resourceFile.delete();
        URL url = classloader.getResource(resourcePath);
        if (url == null) {
            return null;
        }
        InputStream classpathInputStream = url.openStream();
        OutputStream fileOutputStream = new FileOutputStream(resourceFile);
        byte[] buffer = new byte[1024];
        int readByteCount;
        while ((readByteCount = classpathInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, readByteCount);
        }
        classpathInputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
        extendLibPath(parentFile.toString());
        return resourceFile.toString();
    }
