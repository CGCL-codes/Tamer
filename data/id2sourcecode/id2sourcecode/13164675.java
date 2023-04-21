    public void fileUpload() throws Exception {
        status = UploadStatus.UPLOADING;
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(postURL);
        if (login) {
            httppost.setHeader("Cookie", WuploadAccount.getLangcookie() + ";" + WuploadAccount.getSessioncookie() + ";" + WuploadAccount.getMailcookie() + ";" + WuploadAccount.getNamecookie() + ";" + WuploadAccount.getRolecookie() + ";" + WuploadAccount.getOrderbycookie() + ";" + WuploadAccount.getDirectioncookie() + ";");
        } else {
            httppost.setHeader("Cookie", langcookie + ";" + rolecookie + ";");
        }
        MultipartEntity mpEntity = new MultipartEntity();
        mpEntity.addPart("files[]", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().info("Now uploading your file into wupload...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
    }
