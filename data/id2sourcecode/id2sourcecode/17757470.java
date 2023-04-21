    public void run() {
        try {
            if (slingFileAccount.loginsuccessful) {
                host = slingFileAccount.username + " | SlingFile.com";
            } else {
                host = "SlingFile.com";
                uploadFailed();
                return;
            }
            if (file.length() > 2147483648l) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>2GB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            initialize();
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(postURL);
            MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            mpEntity.addPart("Filename", new StringBody(file.getName()));
            mpEntity.addPart("ssd", new StringBody(ssd));
            mpEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
            httppost.setEntity(mpEntity);
            NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
            NULogger.getLogger().info("Now uploading your file into slingfile.com");
            status = UploadStatus.UPLOADING;
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("There might be a problem with your internet connectivity or server problem. Please try again some after time. :(");
            }
            status = UploadStatus.GETTINGLINK;
            NULogger.getLogger().info("Getting download & delete links......");
            u = new URL(postuploadpage);
            uc = (HttpURLConnection) u.openConnection();
            uc.setRequestProperty("Cookie", slingfilecookie.toString());
            uc.setRequestProperty("Referer", sling_guest_url);
            NULogger.getLogger().info(String.valueOf(uc.getResponseCode()));
            br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String temp = "", k = "";
            while ((temp = br.readLine()) != null) {
                k += temp;
            }
            downloadlink = CommonUploaderTasks.parseResponse(k, "this.select();", "\" type=\"text\"");
            downloadlink = downloadlink.replace("\" value=\"", "");
            NULogger.getLogger().log(Level.INFO, "Download link : {0}", downloadlink);
            downURL = downloadlink;
            deletelink = CommonUploaderTasks.parseResponse(k, "Delete Link:", "\" type=\"text");
            deletelink = deletelink.substring(deletelink.indexOf("http://"));
            NULogger.getLogger().log(Level.INFO, "Delete link : {0}", deletelink);
            delURL = deletelink;
            uploadFinished();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            uploadFailed();
        }
    }
