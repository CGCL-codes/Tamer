    public static synchronized File getFileViaHTTPRequest(URL url) throws Exception {
        URLConnection con = url.openConnection();
        if ((con == null) || con.getHeaderField("Content-type").equals("null")) {
            return null;
        }
        File file = File.createTempFile("state", ".zip");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()));
        InputStream in = con.getInputStream();
        byte[] buffer = new byte[1024];
        int numRead;
        while ((numRead = in.read(buffer)) != -1) {
            bos.write(buffer, 0, numRead);
            bos.flush();
        }
        return file;
    }
