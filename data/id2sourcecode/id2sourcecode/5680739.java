    private void verifyPath(String path) throws ModelException {
        try {
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
        } catch (java.io.FileNotFoundException e) {
            final String error = "File not found: " + path;
            throw new ModelException(error);
        } catch (Throwable e) {
            final String error = "Invalid path: " + path;
            throw new ModelException(error, e);
        }
    }
