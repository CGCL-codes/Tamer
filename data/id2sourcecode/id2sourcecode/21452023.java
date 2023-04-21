    private void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        ext = ext.toLowerCase();
        HttpPost httppost = null;
        if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("bmp") || ext.equals("gif") || ext.equals("png") || ext.equals("tiff")) {
            httppost = new HttpPost("http://www.imageshack.us/upload_api.php");
        }
        if (ext.equals("avi") || ext.equals("mkv") || ext.equals("mpeg") || ext.equals("mp4") || ext.equals("mov") || ext.equals("3gp") || ext.equals("flv") || ext.equals("3gpp")) {
            httppost = new HttpPost("http://render.imageshack.us/upload_api.php");
        }
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("Filename", new StringBody(file.getName()));
        reqEntity.addPart("optimage", new StringBody("1"));
        reqEntity.addPart("new_flash_uploader", new StringBody("y"));
        reqEntity.addPart("rembar", new StringBody("0"));
        reqEntity.addPart("myimages", new StringBody("null"));
        reqEntity.addPart("optsize", new StringBody("optimize"));
        reqEntity.addPart("rem_bar", new StringBody("0"));
        if (imageShackAccount.loginsuccessful) {
            reqEntity.addPart("isUSER", new StringBody(ImageShackAccount.getUsercookie()));
            reqEntity.addPart("myimages", new StringBody(ImageShackAccount.getMyimagescookie()));
        } else {
            reqEntity.addPart("isUSER", new StringBody("null"));
        }
        reqEntity.addPart("swfupload", new StringBody("1"));
        reqEntity.addPart("ulevel", new StringBody("null"));
        reqEntity.addPart("always_opt", new StringBody("null"));
        reqEntity.addPart("key", new StringBody(upload_key));
        reqEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
        reqEntity.addPart("upload", new StringBody("Submit Query"));
        httppost.setEntity(reqEntity);
        NULogger.getLogger().info("Now uploading your file into imageshack.us Please wait......................");
        status = UploadStatus.UPLOADING;
        HttpResponse response = httpclient.execute(httppost);
        status = UploadStatus.GETTINGLINK;
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            downloadlink = EntityUtils.toString(resEntity);
            downloadlink = CommonUploaderTasks.parseResponse(downloadlink, "<image_link>", "<");
            NULogger.getLogger().log(Level.INFO, "Download Link : {0}", downloadlink);
            downURL = downloadlink;
            uploadFinished();
        } else {
            throw new Exception("Temporary ImageShack server problem or network problem");
        }
    }
