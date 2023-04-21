    private void uploadLogin() {
        try {
            status = UploadStatus.INITIALISING;
            ukeycookie = MediaFireAccount.getUKeyCookie();
            skeycookie = MediaFireAccount.getSKeyCookie();
            usercookie = MediaFireAccount.getUserCookie();
            status = UploadStatus.GETTINGCOOKIE;
            NULogger.getLogger().log(Level.INFO, "uploadkey {0}", uploadkey);
            NULogger.getLogger().info("Getting MFULConfig value........");
            getMFULConfig();
            postURL = "http://www.mediafire.com/douploadtoapi/?type=basic&" + ukeycookie + "&" + usercookie + "&uploadkey=myfiles&filenum=0&uploader=0&MFULConfig=" + mfulconfig;
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
                NULogger.getLogger().log(Level.INFO, "upload response key {0}", uploadresponsekey);
            }
            getDownloadLink();
            uploadFinished();
        } catch (Exception ex) {
            ex.printStackTrace();
            NULogger.getLogger().severe(ex.toString());
            uploadFailed();
        }
    }
