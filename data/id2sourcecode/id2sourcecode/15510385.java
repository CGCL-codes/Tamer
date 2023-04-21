    private void saveResponseToFile(URL url, File tempFile, Proxy proxy) throws Exception {
        URLConnection connection = null;
        if (proxy == null) {
            connection = url.openConnection();
        } else {
            connection = url.openConnection(proxy);
        }
        InputStream inputStream = connection.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[8];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
    }
