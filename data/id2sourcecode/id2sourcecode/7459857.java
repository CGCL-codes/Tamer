    private void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://flameupload.com/cgi/ubr_upload.pl?upload_id=" + uploadid);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addPart("upfile_0", new MonitoredFileBody(file, uploadProgress));
        mpEntity.addPart("uploaded", new StringBody("on"));
        mpEntity.addPart("wupload", new StringBody("on"));
        mpEntity.addPart("turbobit", new StringBody("on"));
        mpEntity.addPart("depositfiles", new StringBody("on"));
        mpEntity.addPart("fileserve", new StringBody("on"));
        mpEntity.addPart("easyshare", new StringBody("on"));
        mpEntity.addPart("zshare", new StringBody("on"));
        mpEntity.addPart("badongo", new StringBody("on"));
        mpEntity.addPart("filefactory", new StringBody("on"));
        mpEntity.addPart("netload", new StringBody("on"));
        mpEntity.addPart("loadto", new StringBody("on"));
        mpEntity.addPart("_2shared", new StringBody("on"));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
        NULogger.getLogger().info("Now uploading your file into flameupload.com");
        status = UploadStatus.UPLOADING;
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        NULogger.getLogger().info(EntityUtils.toString(resEntity));
        if (response.getStatusLine().getStatusCode() == 302) {
            NULogger.getLogger().info("Files uploaded successfully");
        } else {
            throw new Exception("There might be a problem with your internet connection or server error. Please try again later :(");
        }
    }
