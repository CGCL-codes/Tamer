    protected OutputStream createOutputStream(String nestedURL) throws IOException {
        URL url = new URL(nestedURL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        return urlConnection.getOutputStream();
    }
