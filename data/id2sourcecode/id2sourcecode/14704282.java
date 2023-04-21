    private void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        if (badongoAccount.loginsuccessful) {
            postURL = "http://upload.badongo.com/mpu_upload.php";
        }
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addPart("Filename", new StringBody(file.getName()));
        if (badongoAccount.loginsuccessful) {
            mpEntity.addPart("PHPSESSID", new StringBody(dataid));
        }
        mpEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
        NULogger.getLogger().info("Now uploading your file into badongo.com");
        status = UploadStatus.UPLOADING;
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().log(Level.INFO, "Upload response : {0}", uploadresponse);
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
        }
        NULogger.getLogger().log(Level.INFO, "res {0}", uploadresponse);
        httpclient.getConnectionManager().shutdown();
    }
