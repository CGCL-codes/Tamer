    public void run() {
        try {
            if (zohoDocsAccount.loginsuccessful) {
                host = zohoDocsAccount.username + " | ZohoDocs.com";
            } else {
                host = "ZohoDocs.com";
                uploadFailed();
                return;
            }
            if (file.length() > 52428800) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>50MB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost("https://docs.zoho.com/uploadsingle.do?isUploadStatus=false&folderId=0&refFileElementId=refFileElement0");
            httppost.setHeader("Cookie", ZohoDocsAccount.getZohodocscookies().toString());
            MultipartEntity mpEntity = new MultipartEntity();
            mpEntity.addPart("multiupload_file", new MonitoredFileBody(file, uploadProgress));
            httppost.setEntity(mpEntity);
            NULogger.getLogger().info("Now uploading your file into zoho docs...........................");
            status = UploadStatus.UPLOADING;
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            NULogger.getLogger().info(response.getStatusLine().toString());
            if (resEntity != null) {
                if (EntityUtils.toString(resEntity).contains("File Uploaded Successfully")) {
                    NULogger.getLogger().info("File Uploaded Successfully");
                    uploadFinished();
                } else {
                    throw new Exception("There might be a problem with your internet connection or server error. Please try after some time. :(");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RapidShare.class.getName()).log(Level.SEVERE, null, e);
            uploadFailed();
        }
    }
