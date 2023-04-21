    public File download(String downloadFrom) throws IOException {
        File file = null;
        GetMethod get = new GetMethod(downloadFrom);
        useCurrentSession(get);
        http.executeMethod(get);
        long contentLength = get.getResponseContentLength();
        if (contentLength < Integer.MAX_VALUE) {
            InputStream is = get.getResponseBodyAsStream();
            file = createFile(getCodeBase().getAuthority(), get.getPath());
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[1024];
            int read = 0;
            while ((read = is.read(data)) > -1) {
                os.write(data, 0, read);
            }
            os.flush();
            is.close();
            os.close();
        }
        get.releaseConnection();
        return file;
    }
