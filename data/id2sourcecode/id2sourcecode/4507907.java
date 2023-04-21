    private void getFromURL(String URL, OutputStream os) throws Exception {
        URL url = new URL(URL);
        InputStream is = url.openConnection().getInputStream();
        copyDocument(is, os);
        is.close();
        os.close();
    }
