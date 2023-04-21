    private void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addPart("Filename", new StringBody(file.getName()));
        mpEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
        NULogger.getLogger().info("Now uploading your file into ugotfile.com");
        status = UploadStatus.UPLOADING;
        HttpResponse response = httpclient.execute(httppost);
        status = UploadStatus.GETTINGLINK;
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (response != null) {
            uploadresponse = EntityUtils.toString(response.getEntity());
        }
        NULogger.getLogger().log(Level.INFO, "Upload Response : {0}", uploadresponse);
        downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "[\"", "\"");
        downloadlink = downloadlink.replaceAll("\\\\/", "/");
        deletelink = CommonUploaderTasks.parseResponse(uploadresponse, "\",\"", "\"");
        deletelink = deletelink.replaceAll("\\\\/", "/");
        downURL = downloadlink;
        delURL = deletelink;
        NULogger.getLogger().log(Level.INFO, "Download Link : {0}", downloadlink);
        NULogger.getLogger().log(Level.INFO, "Delete Link : {0}", deletelink);
        uploadFinished();
    }
