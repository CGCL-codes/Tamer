    @Override
    public InputStream openInputStream(String filepath) throws IOException {
        if (filepath.length() == 0) throw new IOException("Empty filepath is not allowed!");
        String classpathFilePath = filepath;
        if (classpathFilePath.charAt(0) != '/') classpathFilePath = '/' + classpathFilePath;
        URL url = getClass().getResource(classpathFilePath);
        if (url != null) {
            return url.openStream();
        } else if (codeBase != null) {
            URL externURL = new URL(codeBase, filepath);
            try {
                return externURL.openStream();
            } catch (IOException ex) {
                throw new IOException("\"" + filepath + "\" not found!", ex);
            }
        } else {
            throw new FileNotFoundException("\"" + filepath + "\" not found!");
        }
    }
