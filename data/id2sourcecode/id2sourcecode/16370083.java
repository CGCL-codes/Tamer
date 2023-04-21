    public boolean readFile(String remoteUrl, String filePath, String fileName) {
        boolean bea = false;
        java.net.URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        if (fileName == null || fileName.equals("")) fileName = Utility.getUrlFileName(remoteUrl);
        File f = new File(filePath, fileName);
        try {
            urlfile = new java.net.URL(remoteUrl);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
        } catch (Exception e) {
            logger.error("远程文件读取失败", e);
        }
        try {
            bos = new BufferedOutputStream(new FileOutputStream(f));
            byte[] buf = new byte[1024];
            int bufsize = 0;
            while ((bufsize = bis.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, bufsize);
            }
            bea = true;
            logger.info(remoteUrl + " 采集成功！文件已存储至：" + filePath + fileName);
        } catch (IOException e) {
            bea = false;
            logger.error(remoteUrl + "采集失败", e);
        } finally {
            try {
                bos.flush();
                bis.close();
                httpUrl.disconnect();
            } catch (Exception e) {
                logger.error("关闭HTTP连接失败", e);
            }
        }
        return bea;
    }
