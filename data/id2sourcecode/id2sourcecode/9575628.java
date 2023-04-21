    public static OutputStream openLog(URL url) throws FileNotFoundException, UnknownHostException, UnknownServiceException, IOException {
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);
        return urlc.getOutputStream();
    }
