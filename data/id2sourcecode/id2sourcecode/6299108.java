    public void retrieveBinaryToFile(String urlToRetrieve, String fileDest) throws MalformedURLException, IOException {
        InputStream is = prepareInputStream(urlToRetrieve);
        OutputStream os = new FileOutputStream(fileDest);
        int nread;
        final int LEN = 65536;
        byte buff[] = new byte[LEN];
        while ((nread = is.read(buff, 0, LEN)) != -1) {
            if (nread > 0) os.write(buff, 0, nread);
        }
        is.close();
        os.close();
    }
