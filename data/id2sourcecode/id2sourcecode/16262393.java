    protected OutputStream createURLOutputStream(URI uri) throws IOException {
        try {
            URL url = new URL(uri.toString());
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            return urlConnection.getOutputStream();
        } catch (RuntimeException exception) {
            throw new Resource.IOWrappedException(exception);
        }
    }
