    public void run() {
        try {
            if (crockoAccount.loginsuccessful) {
                host = crockoAccount.username + " | Crocko.com";
            } else {
                host = "Crocko.com";
            }
            if (file.length() > 2147483648l) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>2GB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            if (crockoAccount.loginsuccessful) {
                getData();
            } else {
                initialize();
            }
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(postURL);
            MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            mpEntity.addPart("Filename", new StringBody(file.getName()));
            if (crockoAccount.loginsuccessful) {
                NULogger.getLogger().info("adding php sess .............");
                mpEntity.addPart("PHPSESSID", new StringBody(CrockoAccount.getSessionid()));
            }
            mpEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
            httppost.setEntity(mpEntity);
            NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
            NULogger.getLogger().info("Now uploading your file into crocko");
            status = UploadStatus.UPLOADING;
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            NULogger.getLogger().info(response.getStatusLine().toString());
            if (resEntity != null) {
                uploadresponse = EntityUtils.toString(resEntity);
            } else {
                throw new Exception("There might be a problem with your internet connectivity or server error. Please try again later :(");
            }
            status = UploadStatus.GETTINGLINK;
            NULogger.getLogger().log(Level.INFO, "Upload response : {0}", uploadresponse);
            downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "Download link:", "</dd>");
            downloadlink = CommonUploaderTasks.parseResponse(downloadlink, "value=\"", "\"");
            deletelink = CommonUploaderTasks.parseResponse(uploadresponse, "Delete link:", "</a></dd>");
            deletelink = deletelink.substring(deletelink.indexOf("http://"));
            NULogger.getLogger().log(Level.INFO, "Download link : {0}", downloadlink);
            NULogger.getLogger().log(Level.INFO, "Delete link : {0}", deletelink);
            downURL = downloadlink;
            delURL = deletelink;
            httpclient.getConnectionManager().shutdown();
            uploadFinished();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            uploadFailed();
        } finally {
            uc = null;
            br = null;
            postURL = null;
            uploadresponse = null;
            downloadlink = null;
            u = null;
            deletelink = null;
        }
    }
