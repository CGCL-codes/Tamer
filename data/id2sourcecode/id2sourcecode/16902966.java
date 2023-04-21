    public void getDownloadInfo() throws Exception {
        URL url = new URL(getURL());
        con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "test");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("Range", "bytes=0-");
        con.setRequestMethod("HEAD");
        con.setUseCaches(false);
        con.connect();
        con.disconnect();
        if (mustRedirect()) secureRedirect();
        AuthManager.putAuth(getSite(), auth);
        url = con.getURL();
        setURL(url.toString());
        setSize(Long.parseLong(con.getHeaderField("Content-Length")));
        setResumable(con.getResponseCode() == 206);
        setLastModified(con.getLastModified());
        setRangeEnd(getSize() - 1);
    }
