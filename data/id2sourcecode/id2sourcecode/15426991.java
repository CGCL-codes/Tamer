    private void uploadWithoutLogin() {
        try {
            if (file.length() > 209715200) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>200MB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            initWithoutLogin();
            status = UploadStatus.UPLOADING;
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost(postURL);
            NULogger.getLogger().info(ukeycookie);
            httppost.setHeader("Cookie", ukeycookie + ";" + skeycookie + ";" + usercookie);
            MultipartEntity mpEntity = new MultipartEntity();
            MonitoredFileBody cbFile = new MonitoredFileBody(file, uploadProgress);
            mpEntity.addPart("", cbFile);
            httppost.setEntity(mpEntity);
            NULogger.getLogger().info("Now uploading your file into mediafire...........................");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            NULogger.getLogger().info(response.getStatusLine().toString());
            if (resEntity != null) {
                NULogger.getLogger().info("Getting upload response key value..........");
                uploadresponsekey = EntityUtils.toString(resEntity);
                getUploadResponseKey();
                NULogger.getLogger().log(Level.INFO, "upload resoponse key {0}", uploadresponsekey);
            }
            getDownloadLink();
            uploadFinished();
        } catch (Exception ex) {
            ex.printStackTrace();
            uploadFailed();
        }
    }
