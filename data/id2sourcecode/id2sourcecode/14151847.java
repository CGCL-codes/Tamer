    public void fileUpload() throws Exception {
        status = UploadStatus.UPLOADING;
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", DepositFilesAccount.getUprandcookie() + ";" + DepositFilesAccount.getAutologincookie());
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new MonitoredFileBody(file, uploadProgress);
        mpEntity.addPart("MAX_FILE_SIZE", new StringBody("2097152000"));
        mpEntity.addPart("UPLOAD_IDENTIFIER", new StringBody(uid));
        mpEntity.addPart("go", new StringBody("1"));
        mpEntity.addPart("files", cbFile);
        httppost.setEntity(mpEntity);
        NULogger.getLogger().info("Now uploading your file into depositfiles...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            status = UploadStatus.GETTINGLINK;
            uploadresponse = EntityUtils.toString(resEntity);
            downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "ud_download_url = '", "'");
            deletelink = CommonUploaderTasks.parseResponse(uploadresponse, "ud_delete_url = '", "'");
            NULogger.getLogger().log(Level.INFO, "download link : {0}", downloadlink);
            NULogger.getLogger().log(Level.INFO, "delete link : {0}", deletelink);
            downURL = downloadlink;
            delURL = deletelink;
        } else {
            throw new Exception();
        }
    }
