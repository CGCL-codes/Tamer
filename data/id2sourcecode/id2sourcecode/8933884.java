    public void fileUpload() throws Exception {
        DefaultHttpClient httpclient;
        httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(localhostrurl);
        MultipartEntity mpEntity = new MultipartEntity();
        mpEntity.addPart("name", new StringBody(file.getName()));
        if (localhostrAccount.loginsuccessful) {
            mpEntity.addPart("session", new StringBody(LocalhostrAccount.getSessioncookie().substring(LocalhostrAccount.getSessioncookie().indexOf("=") + 2)));
        }
        mpEntity.addPart("file", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into localhostr...........................");
        HttpResponse response = httpclient.execute(httppost);
        status = UploadStatus.GETTINGLINK;
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        httpclient.getConnectionManager().shutdown();
        if (resEntity != null) {
            String tmp = EntityUtils.toString(resEntity);
            downloadlink = CommonUploaderTasks.parseResponse(tmp, "\"url\":\"", "\"");
            NULogger.getLogger().log(Level.INFO, "download link : {0}", downloadlink);
            downURL = downloadlink;
            uploadFinished();
        } else {
            throw new Exception("There might be a problem with your internet connection. Please try after some time. :(");
        }
    }
