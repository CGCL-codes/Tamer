    protected InputStream createURLInputStream(URI uri) throws IOException {
        try {
            URL url = new URL(uri.toString());
            URLConnection urlConnection = url.openConnection();
            return urlConnection.getInputStream();
        } catch (RuntimeException exception) {
            throw new Resource.IOWrappedException(exception);
        }
    }
