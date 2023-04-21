    public static InputStream streamFromPath(String path) throws IOException {
        Object obj;
        if (codeBaseURL == null) {
            obj = new BufferedInputStream(new FileInputStream(path));
        } else {
            URL url = new URL(codeBaseURL, path);
            obj = url.openStream();
        }
        return ((InputStream) (obj));
    }
