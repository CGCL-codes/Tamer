    public void run() {
        try {
            if (localhostrAccount.loginsuccessful) {
                host = localhostrAccount.username + " | Localhostr.com";
            } else {
                host = "Localhostr.com";
            }
            long length_limit;
            if (localhostrAccount.loginsuccessful) {
                length_limit = 1073741824;
            } else {
                length_limit = 52428800;
            }
            if (file.length() > length_limit) {
                if (localhostrAccount.loginsuccessful) {
                    JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>1GB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>50MB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                }
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            initialize();
            HttpOptions httpoptions = new HttpOptions(localhostrurl);
            DefaultHttpClient httpclient;
            httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpResponse myresponse = httpclient.execute(httpoptions);
            HttpEntity myresEntity = myresponse.getEntity();
            NULogger.getLogger().info(EntityUtils.toString(myresEntity));
            httpclient.getConnectionManager().shutdown();
            fileUpload();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            uploadFailed();
        }
    }
