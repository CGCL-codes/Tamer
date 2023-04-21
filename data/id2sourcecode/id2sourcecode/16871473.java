    public static HttpResponse makePostAttachments(String url, String token, DefaultHttpClient httpclient, File aFile, String aFileName) throws Exception {
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("X-TrackerToken", token);
        httppost.removeHeaders("Connection");
        MultipartEntity mpEntity = new MultipartEntity();
        File f = aFile;
        if (aFileName != null && aFileName.length() > 0) {
            File newFile = new File(f.getParentFile(), aFileName);
            boolean renameSuccess = f.renameTo(newFile);
            if (renameSuccess) {
                f = newFile;
            } else {
                newFile = new File(f.getParentFile(), "" + (System.currentTimeMillis() % 1000000) + aFileName);
                renameSuccess = f.renameTo(newFile);
                if (renameSuccess) {
                    f = newFile;
                }
            }
        }
        ContentBody cbFile = new FileBody(f, "binary/octet-stream");
        mpEntity.addPart("Filedata", cbFile);
        httppost.setEntity(mpEntity);
        HttpResponse response = httpclient.execute(httppost);
        return response;
    }
