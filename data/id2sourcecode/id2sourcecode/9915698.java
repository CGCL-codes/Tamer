    public static InputStream getInputStream(String path, ClassLoader loader) {
        URL url = ClassUtils.getURL(getClassPathToResourcePath(path), loader);
        InputStream in = null;
        try {
            in = url.openStream();
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException(path + " is not found.");
        }
        return in;
    }
