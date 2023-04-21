    private void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", ucookie);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("UPLOAD_IDENTIFIER", new StringBody(uid));
        reqEntity.addPart("u", new StringBody(ucookie));
        reqEntity.addPart("file_0", new MonitoredFileBody(file, uploadProgress));
        reqEntity.addPart("service_1", new StringBody("1"));
        reqEntity.addPart("service_16", new StringBody("1"));
        reqEntity.addPart("service_7", new StringBody("1"));
        reqEntity.addPart("service_17", new StringBody("1"));
        reqEntity.addPart("service_9", new StringBody("1"));
        reqEntity.addPart("service_6", new StringBody("1"));
        reqEntity.addPart("service_15", new StringBody("1"));
        reqEntity.addPart("service_14", new StringBody("1"));
        reqEntity.addPart("service_18", new StringBody("1"));
        reqEntity.addPart("remember_1", new StringBody("1"));
        reqEntity.addPart("remember_16", new StringBody("1"));
        reqEntity.addPart("remember_7", new StringBody("1"));
        reqEntity.addPart("remember_17", new StringBody("1"));
        reqEntity.addPart("remember_9", new StringBody("1"));
        reqEntity.addPart("remember_6", new StringBody("1"));
        reqEntity.addPart("remember_15", new StringBody("1"));
        reqEntity.addPart("remember_14", new StringBody("1"));
        reqEntity.addPart("remember_18", new StringBody("1"));
        httppost.setEntity(reqEntity);
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into multiupload.com. Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
        status = UploadStatus.GETTINGLINK;
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            uploadresponse = CommonUploaderTasks.parseResponse(uploadresponse, "\"downloadid\":\"", "\"");
            downloadlink = "http://www.multiupload.com/" + uploadresponse;
            NULogger.getLogger().log(Level.INFO, "Download link : {0}", downloadlink);
            downURL = downloadlink;
        } else {
            throw new Exception("MultiUpload server problem or Internet connectivity problem");
        }
    }
