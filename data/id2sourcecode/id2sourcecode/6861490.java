    public int downloadFile(String path, String fileName, String urlString) throws IOException {
        FileUtils fileUtils = new FileUtils();
        int flag = fileUtils.createSDCardFile(path, fileName);
        if (flag == AppConstant.DownloadVars.SUCCESS) {
            File file = new File(fileUtils.getSDPATH() + path + File.separator + fileName);
            URL url = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            if (urlConn.getResponseCode() != 404) {
                BufferedOutputStream outPut = new BufferedOutputStream(new FileOutputStream(file));
                BufferedInputStream inPut = new BufferedInputStream(urlConn.getInputStream());
                byte[] buffer = new byte[1024 * 1024];
                int hasRead = 0;
                while ((hasRead = inPut.read(buffer, 0, buffer.length)) != -1) {
                    outPut.write(buffer, 0, hasRead);
                    outPut.flush();
                }
                inPut.close();
                outPut.close();
            } else {
                return AppConstant.DownloadVars.NO_FILE;
            }
        }
        return flag;
    }
