    private void fileupload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://upload.filefactory.com/upload.php");
        if (login) {
            httppost.setHeader("Cookie", fileFactoryAccount.getFilecookie() + ";" + fileFactoryAccount.getMembershipcookie());
        } else {
            httppost.setHeader("Cookie", filecookie);
        }
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
        reqEntity.addPart("upload", new StringBody("Submit Query"));
        httppost.setEntity(reqEntity);
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into filefactory.com. Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        String page = "";
        if (resEntity != null) {
            page = EntityUtils.toString(resEntity);
            NULogger.getLogger().log(Level.INFO, "Page URL value :{0}", page);
        }
        status = UploadStatus.GETTINGLINK;
        downloadlink = getData("http://www.filefactory.com/mupc/" + page);
        downloadlink = downloadlink.substring(downloadlink.indexOf("<div class=\"metadata\">"));
        downloadlink = downloadlink.replace("<div class=\"metadata\">", "");
        downloadlink = downloadlink.substring(0, downloadlink.indexOf("<"));
        downloadlink = downloadlink.trim();
        NULogger.getLogger().log(Level.INFO, "Download Link : {0}", downloadlink);
        downURL = downloadlink;
        uploadFinished();
    }
