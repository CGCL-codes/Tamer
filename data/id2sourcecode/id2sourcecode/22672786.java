    public void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(oronlink);
        if (oronAccount.loginsuccessful) {
            httppost.setHeader("Cookie", OronAccount.getLogincookie() + ";" + OronAccount.getXfsscookie());
        }
        MultipartEntity mpEntity = new MultipartEntity();
        mpEntity.addPart("upload_type", new StringBody("file"));
        mpEntity.addPart("srv_id", new StringBody(serverID));
        if (oronAccount.loginsuccessful) {
            mpEntity.addPart("sess_id", new StringBody(OronAccount.getXfsscookie().substring(5)));
        }
        mpEntity.addPart("srv_tmp_url", new StringBody(tmpserver));
        mpEntity.addPart("file_0", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().info("Now uploading your file into oron...........................");
        status = UploadStatus.UPLOADING;
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            String tmp = EntityUtils.toString(resEntity);
            NULogger.getLogger().log(Level.INFO, "Upload response : {0}", tmp);
            fnvalue = CommonUploaderTasks.parseResponse(tmp, "name='fn' value='", "'");
            NULogger.getLogger().log(Level.INFO, "fn value : {0}", fnvalue);
        } else {
            throw new Exception("There might be a problem with your internet connection or server problem. Please try again after some time. :(");
        }
    }
