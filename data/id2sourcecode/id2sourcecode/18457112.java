    public boolean canRead(URL url) {
        try {
            final URLConnection urlConnection = url.openConnection();
            return urlConnection.getDoInput();
        } catch (Exception e) {
            return false;
        }
    }
