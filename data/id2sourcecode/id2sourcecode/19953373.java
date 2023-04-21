    public void executeUploadOperation() throws Exception {
        if (uploadOperation == null) throw new NullPointerException("Upload operation that is to be executed cannot be null");
        throwExceptionIfAlreadyRunning();
        started = true;
        LoginRequest loginRequest = getUploaderAccount().getLoginRequest(getHttpClient().getCookieStore());
        getHttpClient().execute(loginRequest.getPreLoginRequest()).getEntity().consumeContent();
        HttpResponse response = getHttpClient().execute(loginRequest.getHttpUriRequest());
        response.getEntity().consumeContent();
        getUploaderAccount().setLoginResponse(new HttpLoginResponse(response, getHttpClient().getCookieStore().getCookies()));
        for (Cookie cookie : getHttpClient().getCookieStore().getCookies()) {
            if (cookie.getName().equals("ukey") && cookie.getDomain().contains("mediafire")) {
                ukey = cookie;
            }
        }
        String uploaderkey = null;
        uploaderkey = getDestinationDirectory() == null ? null : getDestinationDirectory().getQuickKey();
        for (Cookie cookie : getHttpClient().getCookieStore().getCookies()) {
            if (cookie.getName().equals("user") && cookie.getDomain().contains("mediafire")) {
                user = cookie;
            }
        }
        if (user == null) {
            throw new RuntimeException("Could not initialize user cookie");
        }
        if (uploaderkey == null) {
        }
        getDirectories();
        MultipartEntity request = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        request.addPart("Filedata", new InputStreamBody(uploadOperation.getContentBeingUploaded().getInputStream(), "application/octet-stream", super.getDestinationFileName()));
        if (destinationDirectory == null) {
            throw new NullPointerException("wth");
        }
        System.out.println("destdir");
        System.out.println(destinationDirectory);
        System.out.println(destinationDirectory.getKey());
        HttpPost uploadRequest = new HttpPost(UPLOAD_URL + "?ukey=" + ukey.getValue() + "&user=" + user.getValue() + "&uploadkey=" + destinationDirectory.getKey() + "&filenum=0&uploader=0&MFU");
        uploadRequest.setEntity(request);
        uploadRequest.addHeader(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        uploadRequest.addHeader(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
        HttpResponse uploadResponse = getHttpClient().execute(uploadRequest);
        Object[] o = getUploadKey(uploadResponse);
        String uploadKey = (String) o[1];
        monitorUploadProgress(uploadKey);
    }
