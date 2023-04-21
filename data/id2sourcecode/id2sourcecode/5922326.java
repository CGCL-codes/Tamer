    private void uploadWithoutLogin() {
        try {
            if (file.length() > 1073741824) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>1GB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://www.easy-share.com");
            httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2 GTBDFff GTB7.0");
            HttpResponse httpresponse = httpclient.execute(httpget);
            EntityUtils.consume(httpresponse.getEntity());
            httppost = new HttpPost("http://upload.easy-share.com/accounts/upload_backend/perform/ajax");
            httppost.setHeader("User-Agent", "Shockwave Flash");
            MultipartEntity requestEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            requestEntity.addPart("Filename", new StringBody(file.getName()));
            requestEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
            requestEntity.addPart("Upload", new StringBody("Submit Query"));
            httppost.setEntity(requestEntity);
            status = UploadStatus.UPLOADING;
            httpresponse = httpclient.execute(httppost);
            String strResponse = EntityUtils.toString(httpresponse.getEntity());
            status = UploadStatus.GETTINGLINK;
            downURL = strResponse.substring(strResponse.indexOf("value=\"") + 7);
            downURL = downURL.substring(0, downURL.indexOf("\""));
            delURL = strResponse.substring(strResponse.lastIndexOf("javascript:;\">") + 14);
            delURL = delURL.substring(0, delURL.indexOf("</a>"));
            NULogger.getLogger().log(Level.INFO, "Download Link: {0}", downURL);
            NULogger.getLogger().log(Level.INFO, "Delete link: {0}", delURL);
            uploadFinished();
        } catch (Exception ex) {
            ex.printStackTrace();
            NULogger.getLogger().severe(ex.toString());
            uploadFailed();
        }
    }
