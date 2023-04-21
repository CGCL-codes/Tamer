    @Override
    public void run() {
        try {
            if (iFileAccount.loginsuccessful) {
                host = iFileAccount.username + " | IFile.it";
            } else {
                host = "IFile.it";
                uploadFailed();
                return;
            }
            if (file.length() > 1090519040) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>1040MB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            NULogger.getLogger().info("Getting upload url from ifile.......");
            u = new URL("http://ifile.it/api-fetch_upload_url.api");
            uc = (HttpURLConnection) u.openConnection();
            br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String temp = "", k = "";
            while ((temp = br.readLine()) != null) {
                NULogger.getLogger().info(temp);
                k += temp;
            }
            uc.disconnect();
            postURL = CommonUploaderTasks.parseResponse(k, "upload_url\":\"", "\"");
            postURL = postURL.replaceAll("\\\\", "");
            NULogger.getLogger().log(Level.INFO, "Post URL : {0}", postURL);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(postURL);
            MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            mpEntity.addPart("akey", new StringBody(NeembuuUploaderProperties.getEncryptedProperty("ifile_api_key")));
            mpEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
            httppost.setEntity(mpEntity);
            NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
            NULogger.getLogger().info("Now uploading your file into ifile.it");
            status = UploadStatus.UPLOADING;
            HttpResponse response = httpclient.execute(httppost);
            status = UploadStatus.GETTINGLINK;
            NULogger.getLogger().info("Now getting downloading link.........");
            HttpEntity resEntity = response.getEntity();
            httpclient.getConnectionManager().shutdown();
            NULogger.getLogger().info(response.getStatusLine().toString());
            if (resEntity != null) {
                uploadresponse = EntityUtils.toString(resEntity);
                NULogger.getLogger().log(Level.INFO, "Upload response : {0}", uploadresponse);
            } else {
                throw new Exception("There might be a problem with your internet connection or server error. Please try after some time :(");
            }
            if (uploadresponse.contains("\"status\":\"ok\"")) {
                NULogger.getLogger().info("File uploaded successfully :)");
                file_ukey = CommonUploaderTasks.parseResponse(uploadresponse, "\"ukey\":\"", "\"");
                NULogger.getLogger().log(Level.INFO, "File ukey : {0}", file_ukey);
                downloadlink = "http://www.ifile.it/" + file_ukey + "/" + file.getName();
                downURL = downloadlink;
                NULogger.getLogger().log(Level.INFO, "Download link : {0}", downloadlink);
            } else {
                throw new Exception("There might be a problem with your internet connection or server error. Please try after some time :(");
            }
            NULogger.getLogger().log(Level.INFO, "Download Link: {0}", downURL);
            uploadFinished();
        } catch (Exception ex) {
            ex.printStackTrace();
            NULogger.getLogger().severe(ex.toString());
            uploadFailed();
        }
    }
