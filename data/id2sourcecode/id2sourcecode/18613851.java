    public boolean canWrite(URL url) {
        try {
            final URLConnection urlConnection = url.openConnection();
            return urlConnection.getDoOutput();
        } catch (Exception e) {
            return false;
        }
    }
