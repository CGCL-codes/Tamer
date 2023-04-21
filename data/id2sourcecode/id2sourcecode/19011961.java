    public void downloadFile(String remoteName, String localFile) throws Exception {
        InputStream is = ftpClient.retrieveFileStream(remoteName);
        byte[] buf = new byte[1024];
        int size = 0;
        BufferedInputStream bis = new BufferedInputStream(is);
        FileOutputStream fos = new FileOutputStream(localFile);
        while ((size = bis.read(buf)) != -1) fos.write(buf, 0, size);
        fos.close();
        bis.close();
    }
