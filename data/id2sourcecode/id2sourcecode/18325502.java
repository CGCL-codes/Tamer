    public boolean doDownload() throws FileNotFoundException, IOException {
        if (downloadDir == null || !downloadDir.exists()) {
            throw new IllegalArgumentException("Cannot have a non-existent download directory");
        }
        File f = new File(downloadDir, fileName);
        FileOutputStream fos = new FileOutputStream(f);
        GetMethod getMethod = new GetMethod(url);
        int respCode = httpClient.executeMethod(getMethod);
        if (respCode != HttpStatus.SC_OK) {
            return false;
        }
        f.createNewFile();
        InputStream is = getMethod.getResponseBodyAsStream();
        long fileSize = getMethod.getResponseContentLength();
        long readSize = 0;
        byte[] buffer = new byte[1024];
        int readLength = -1;
        while ((readLength = is.read(buffer)) != -1) {
            fos.write(buffer, 0, readLength);
            readSize += readLength;
            firePropertyChange(readSize);
        }
        fos.close();
        is.close();
        return true;
    }
