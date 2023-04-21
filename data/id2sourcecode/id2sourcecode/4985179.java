    private void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", UploadingDotComAccount.getSidcookie() + ";" + UploadingDotComAccount.getUcookie() + ";" + UploadingDotComAccount.getCachecookie() + ";" + UploadingDotComAccount.getTimecookie());
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("Filename", new StringBody(getFileName()));
        reqEntity.addPart("SID", new StringBody(sid));
        reqEntity.addPart("folder_id", new StringBody("0"));
        reqEntity.addPart("file", new StringBody(fileID));
        reqEntity.addPart("file", new MonitoredFileBody(file, uploadProgress));
        reqEntity.addPart("upload", new StringBody("Submit Query"));
        httppost.setEntity(reqEntity);
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into uploading.com. Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            NULogger.getLogger().log(Level.INFO, "PAGE :{0}", uploadresponse);
            uploadresponse = CommonUploaderTasks.parseResponse(uploadresponse, "answer\":\"", "\"");
            downURL = downloadlink;
            uploadFinished();
        } else {
            throw new Exception("There might be a problem with your internet connection or server error. Please try after some time :(");
        }
    }
