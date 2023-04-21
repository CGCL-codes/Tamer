    protected InputStream openDownloadStream(String dir, String targetfile) throws IOException {
        URL url = makeURL(dir, targetfile);
        URLConnection urlc = url.openConnection();
        urlc.setDoInput(true);
        InputStream is = urlc.getInputStream();
        return is;
    }
