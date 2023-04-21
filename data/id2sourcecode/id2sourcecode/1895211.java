    private Uploader upload(UploadDestinationHost destinationHost, UploaderAccount account, UploadOperation uploadOperation, UploadProgressListener listener, String destinationFolderName) throws Exception {
        UploaderInstanceProvider uploaderInstanceProvider = getUploaderInstanceProvider(destinationHost);
        if (!uploaderInstanceProvider.canUpload(uploadOperation)) throw new UnsupportedOperationException();
        AbstractHttpClient httpClient = getHttpClientReservedFor(uploadOperation);
        LoginRequest loginRequest = account.getLoginRequest(httpClient.getCookieStore());
        httpClient.execute(loginRequest.getPreLoginRequest()).getEntity().consumeContent();
        HttpResponse response = httpClient.execute(loginRequest.getHttpUriRequest());
        account.setLoginResponse(new HttpLoginResponse(response, httpClient.getCookieStore().getCookies()));
        response.getEntity().consumeContent();
        HttpGet accountRootReq = account.getAccountRootRequest(httpClient.getCookieStore());
        response = httpClient.execute(accountRootReq);
        AccountRoot rootDirectory = account.getAccountRoot(response);
        AccountFileContainer destinationDirectory = null;
        try {
            AccountDirectory accountDirectory = (AccountDirectory) rootDirectory.get(destinationFolderName, FileType.FOLDER);
            if (accountDirectory == null) {
                destinationDirectory = accountDirectory;
            }
        } catch (ClassCastException cce) {
            destinationDirectory = rootDirectory;
        }
        Uploader uploader = uploaderInstanceProvider.getInstance(uploadOperation, account, destinationDirectory);
        return uploader;
    }
