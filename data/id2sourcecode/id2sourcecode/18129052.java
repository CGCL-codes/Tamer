    public static String downloadRes(String urlString, String strFilePath) {
        log.debug("ces.platform.infoplat.utils.HTMLParser.HttpUtils.downloadRes urlString:= " + urlString + "  strFilePath:= " + strFilePath);
        URL url;
        URLConnection connection;
        StringBuffer buffer;
        PrintWriter out;
        String fileName = null;
        try {
            File filePath = new File(strFilePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
            if ((new File(strFilePath + fileName)).exists()) {
                MD5 md5 = new MD5();
                fileName = fileName.substring(0, fileName.indexOf(".")) + String.valueOf(Math.round(Math.random() * 100)) + fileName.substring(fileName.indexOf("."));
                fileName = md5.getMD5ofStr(fileName.substring(0, fileName.indexOf(".")) + String.valueOf((new Date()).getTime())) + fileName.substring(fileName.indexOf("."));
            }
            if (urlString.trim().toLowerCase().startsWith("file")) {
                url = new URL(urlString);
                connection = (FileURLConnection) url.openConnection();
            } else if (urlString.trim().toLowerCase().startsWith("http")) {
                url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
            } else {
                return fileName;
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fo = new FileOutputStream(strFilePath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fo);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bis.close();
            bos.close();
        } catch (Exception e) {
            log.error("������Դʧ��!", e);
        }
        return fileName;
    }
