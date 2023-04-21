    private void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", sidcookie + ";" + ssuicookie);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addPart("MAX_FILE_SIZE", new StringBody("314572800"));
        mpEntity.addPart("UPLOAD_IDENTIFIER", new StringBody(uploadid));
        mpEntity.addPart("DESTINATION_DIR", new StringBody(destinationdir));
        mpEntity.addPart("js_enabled", new StringBody("1"));
        mpEntity.addPart("signature", new StringBody(signature));
        mpEntity.addPart("upload_files", new StringBody(""));
        if (sendSpaceAccount.loginsuccessful) {
            mpEntity.addPart("userid", new StringBody(userid));
        }
        mpEntity.addPart("terms", new StringBody("1"));
        mpEntity.addPart("file[]", new StringBody(""));
        mpEntity.addPart("description[]", new StringBody(""));
        mpEntity.addPart("upload_file[]", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into sendspace.com");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            status = UploadStatus.GETTINGLINK;
            uploadresponse = EntityUtils.toString(resEntity);
        }
        downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "Download Link", "target", false);
        deletelink = CommonUploaderTasks.parseResponse(uploadresponse, "Delete File Link", "target", false);
        downloadlink = downloadlink.replaceAll("\\s+", " ");
        deletelink = deletelink.replaceAll("\\s+", " ");
        downloadlink = CommonUploaderTasks.parseResponse(downloadlink, "<a href=\"", "\"", false);
        deletelink = CommonUploaderTasks.parseResponse(deletelink, "href=\"", "\"", false);
        NULogger.getLogger().log(Level.INFO, "Download link : {0}", downloadlink);
        NULogger.getLogger().log(Level.INFO, "Delete link : {0}", deletelink);
        downURL = downloadlink;
        delURL = deletelink;
        httpclient.getConnectionManager().shutdown();
        uploadFinished();
    }
