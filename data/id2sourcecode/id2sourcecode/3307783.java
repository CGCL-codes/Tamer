    private URLConnection getUploadConnection() throws Exception {
        URL url = new URL(getUploadUrlString());
        System.out.println(getClass().getName() + ".getUploadStream: opening URL: " + url);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        return urlConnection;
    }
