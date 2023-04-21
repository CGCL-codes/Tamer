    public void saveToFile(String destUrl) throws Exception {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        createFile();
        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
        httpUrl.connect();
        bis = new BufferedInputStream(httpUrl.getInputStream());
        fos = new FileOutputStream(path + fileName);
        log.debug("destUrl：[" + destUrl + "]， save to ：[" + path + fileName + "]");
        while ((size = bis.read(buf)) != -1) fos.write(buf, 0, size);
        fos.close();
        bis.close();
        httpUrl.disconnect();
        file = new File(path + fileName);
    }
