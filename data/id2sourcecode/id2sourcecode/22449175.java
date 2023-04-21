    public InputStream getResourceAsStream(String projectRelativePath) {
        try {
            URL url = new URL(root, projectRelativePath);
            return url.openStream();
        } catch (IOException e) {
            logger.error("Cannot find resource for path " + projectRelativePath, e);
            return null;
        }
    }
