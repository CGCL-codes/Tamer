    public void run() {
        try {
            if (gruploadAccount.loginsuccessful) {
                host = gruploadAccount.username + " | GRupload.com";
            } else {
                host = "GRupload.com";
            }
            if (file.length() > 1073741824l) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>1GB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            initialize();
            fileUpload();
            status = UploadStatus.GETTINGLINK;
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            HttpPost httppost = new HttpPost("http://grupload.com/");
            if (gruploadAccount.loginsuccessful) {
                httppost.setHeader("Cookie", GRuploadAccount.getLogincookie() + ";" + GRuploadAccount.getXfsscookie());
            }
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("op", "upload_result"));
            formparams.add(new BasicNameValuePair("fn", fnvalue));
            formparams.add(new BasicNameValuePair("st", "OK"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            uploadresponse = EntityUtils.toString(httpresponse.getEntity());
            downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "Download Link", "</textarea>");
            downloadlink = downloadlink.substring(downloadlink.indexOf("http://"));
            NULogger.getLogger().log(Level.INFO, "Download Link : {0}", downloadlink);
            deletelink = CommonUploaderTasks.parseResponse(uploadresponse, "Delete Link", "</textarea>");
            deletelink = deletelink.substring(deletelink.indexOf("http://"));
            NULogger.getLogger().log(Level.INFO, "Delete Link : {0}", deletelink);
            downURL = downloadlink;
            delURL = deletelink;
            httpclient.getConnectionManager().shutdown();
            uploadFinished();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            uploadFailed();
        }
    }
