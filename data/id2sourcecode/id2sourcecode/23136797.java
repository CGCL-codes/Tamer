    public void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", FileSonicAccount.getLangcookie() + ";" + FileSonicAccount.getSessioncookie() + ";" + FileSonicAccount.getMailcookie() + ";" + FileSonicAccount.getNamecookie() + ";" + FileSonicAccount.getRolecookie() + ";");
        MultipartEntity mpEntity = new MultipartEntity();
        mpEntity.addPart("files[]", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into filesonic...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            tmp = EntityUtils.toString(resEntity);
        }
        uploadresponse = response.getLastHeader("Location").getValue();
        NULogger.getLogger().log(Level.INFO, "Upload response URL : {0}", uploadresponse);
        uploadresponse = getData(uploadresponse);
        if (uploadresponse.contains("File was successfully uploaded")) {
            NULogger.getLogger().info("File was successfully uploaded :)");
            uploadFinished();
        } else {
            throw new Exception("There might be a problem with your internet connecivity or server error. Please try after some time. :(");
        }
    }
