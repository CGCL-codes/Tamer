    public void uploadArchive(URL url, String fileName) throws IOException, JDOMException {
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        try {
            startSession();
            getPhotoStatus(fileName, con.getContentLength());
            uploadArchive(in, fileName, con.getContentLength(), new Date(con.getLastModified()));
            markLastPhotoInRoll();
        } finally {
            in.close();
        }
    }
