    public long getLength(URL url) {
        try {
            final URLConnection urlConnection = url.openConnection();
            return urlConnection.getContentLength();
        } catch (Exception e) {
            return -1;
        }
    }
