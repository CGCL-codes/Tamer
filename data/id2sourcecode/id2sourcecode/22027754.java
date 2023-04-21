    private void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addPart("Filename", new StringBody(file.getName()));
        mpEntity.addPart("notprivate", new StringBody("false"));
        mpEntity.addPart("folder", new StringBody("/"));
        mpEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into zippyshare.com");
        HttpResponse response = httpclient.execute(httppost);
        status = UploadStatus.GETTINGLINK;
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "value=\"http://", "\"");
            downloadlink = "http://" + downloadlink;
            NULogger.getLogger().log(Level.INFO, "Download Link : {0}", downloadlink);
            downURL = downloadlink;
            httpclient.getConnectionManager().shutdown();
        } else {
            throw new Exception("ZippyShare server problem or Internet connectivity problem");
        }
    }
