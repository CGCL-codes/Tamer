    @Override
    public void run() {
        try {
            if (sugarSyncAccount.loginsuccessful) {
                host = sugarSyncAccount.username + " | SugarSync.com";
            } else {
                host = "SugarSync.com";
                uploadFailed();
                return;
            }
            status = UploadStatus.INITIALISING;
            getUserInfo();
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            String CREATE_FILE_REQUEST = String.format(CREATE_FILE_REQUEST_TEMPLATE, file.getName(), ext + " file");
            NULogger.getLogger().info("now creating file request............");
            postData(CREATE_FILE_REQUEST, upload_folder_url);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPut httpput = new HttpPut(SugarSync_File_Upload_URL);
            httpput.setHeader("Authorization", SugarSyncAccount.getAuth_token());
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("", new MonitoredFileBody(file, uploadProgress));
            httpput.setEntity(reqEntity);
            NULogger.getLogger().info("Now uploading your file into sugarsync........ Please wait......................");
            NULogger.getLogger().log(Level.INFO, "Now executing.......{0}", httpput.getRequestLine());
            HttpResponse response = httpclient.execute(httpput);
            NULogger.getLogger().info(response.getStatusLine().toString());
            if (response.getStatusLine().getStatusCode() == 204) {
                NULogger.getLogger().info("File uploaded successfully :)");
                uploadFinished();
            } else {
                throw new Exception("There might be problem with your internet connection or server error. Please try again some after time :(");
            }
        } catch (Exception e) {
            Logger.getLogger(RapidShare.class.getName()).log(Level.SEVERE, null, e);
            uploadFailed();
        }
    }
