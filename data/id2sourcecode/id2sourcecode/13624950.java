    public void fileUpload() throws Exception {
        status = UploadStatus.UPLOADING;
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", FileServeAccount.getDashboardcookie());
        MultipartEntity mpEntity = new MultipartEntity();
        mpEntity.addPart("files", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().info("Now uploading your file into fileserve...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            NULogger.getLogger().log(Level.INFO, "upload response : {0}", uploadresponse);
            status = UploadStatus.GETTINGLINK;
            String shortencode = CommonUploaderTasks.parseResponse(uploadresponse, "\"shortenCode\":\"", "\"");
            String fileName = CommonUploaderTasks.parseResponse(uploadresponse, "\"fileName\":\"", "\"");
            String deleteCode = CommonUploaderTasks.parseResponse(uploadresponse, "\"deleteCode\":\"", "\"");
            downloadlink = "http://www.fileserve.com/file/" + shortencode + "/" + fileName;
            deletelink = "http://www.fileserve.com/file/" + shortencode + "/delete/" + deleteCode;
            NULogger.getLogger().log(Level.INFO, "Download Link : {0}", downloadlink);
            NULogger.getLogger().log(Level.INFO, "Delete Link : {0}", deletelink);
            downURL = downloadlink;
            delURL = deletelink;
            httpclient.getConnectionManager().shutdown();
        } else {
            throw new Exception("There might be a problem with your internet connection or server error. Please try again later :(");
        }
    }
