    private void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        if (uploadBoxAccount.loginsuccessful) {
            httppost.setHeader("Cookie", UploadBoxAccount.getSidcookie());
        } else {
            httppost.setHeader("Cookie", sidcookie);
        }
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addPart("filepc", new MonitoredFileBody(file, uploadProgress));
        mpEntity.addPart("server", new StringBody(server));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
        NULogger.getLogger().info("Now uploading your file into uploadbox.com");
        status = UploadStatus.UPLOADING;
        HttpResponse response = httpclient.execute(httppost);
        status = UploadStatus.GETTINGLINK;
        NULogger.getLogger().info(response.getStatusLine().toString());
        uploadresponse = response.getLastHeader("Location").getValue();
        NULogger.getLogger().log(Level.INFO, "Upload Response : {0}", uploadresponse);
        uploadresponse = getData(uploadresponse);
        downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "name=\"loadlink\" id=\"loadlink\" class=\"text\" onclick=\"this.select();\" value=\"", "\"");
        deletelink = CommonUploaderTasks.parseResponse(uploadresponse, "name=\"deletelink\" id=\"deletelink\" class=\"text\" onclick=\"this.select();\" value=\"", "\"");
        downURL = downloadlink;
        delURL = deletelink;
        NULogger.getLogger().log(Level.INFO, "Download link {0}", downloadlink);
        NULogger.getLogger().log(Level.INFO, "deletelink : {0}", deletelink);
        uploadFinished();
    }
