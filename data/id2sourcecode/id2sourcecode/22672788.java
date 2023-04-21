    public void run() {
        try {
            if (oronAccount.loginsuccessful) {
                host = oronAccount.username + " | Oron.com";
            } else {
                host = "Oron.com";
            }
            if (file.length() > 427622400) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>400MB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            initialize();
            fileUpload();
            status = UploadStatus.GETTINGLINK;
            refererURL = tmpserver + "/status.html?file=" + UploadID + "=" + file.getName();
            NULogger.getLogger().log(Level.INFO, "Referer URL : {0}", refererURL);
            uploadresponse = getData(refererURL);
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            HttpPost httppost = new HttpPost("http://oron.com/");
            if (oronAccount.loginsuccessful) {
                httppost.setHeader("Cookie", OronAccount.getLogincookie() + ";" + OronAccount.getXfsscookie());
            }
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("op", "upload_result"));
            formparams.add(new BasicNameValuePair("fn", fnvalue));
            formparams.add(new BasicNameValuePair("st", "OK"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            uploadresponse = EntityUtils.toString(httpresponse.getEntity());
            if (uploadresponse.isEmpty()) {
                throw new Exception("There might be a problem with your internet connection or Oron server problem. Please try after some time. :(");
            }
            downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "Download Link", "\" class=");
            downloadlink = downloadlink.substring(downloadlink.indexOf("<a href=\""));
            downloadlink = downloadlink.replace("<a href=\"", "");
            NULogger.getLogger().log(Level.INFO, "Download Link : {0}", downloadlink);
            downURL = downloadlink;
            deletelink = CommonUploaderTasks.parseResponse(uploadresponse, "Delete Link", "\"></td>");
            deletelink = deletelink.substring(deletelink.indexOf("value=\""));
            deletelink = deletelink.replace("value=\"", "");
            NULogger.getLogger().log(Level.INFO, "Delete Link : {0}", deletelink);
            delURL = deletelink;
            uploadFinished();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            uploadFailed();
        }
    }
