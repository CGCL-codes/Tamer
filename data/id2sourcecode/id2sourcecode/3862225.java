    public String uploadPhoto(WritePhotoRequest request) throws ApplicationException {
        String responseString = null;
        try {
            OAuthAccessor accessor = new OAuthAccessor(OAuthConnectionManager.consumer);
            accessor.accessToken = ConfigurationManager.instance.getAccessToken();
            accessor.tokenSecret = ConfigurationManager.instance.getAccessTokenSecret();
            String tempImagePath = request.getParameterMap().remove(Constants.TEMP_IMAGE_FILE_PATH);
            String eventId = request.getParameterMap().remove(Constants.EVENT_ID_KEY);
            ArrayList<Map.Entry<String, String>> params = new ArrayList<Map.Entry<String, String>>();
            convertRequestParamsToOAuth(params, request.getParameterMap());
            OAuthMessage message = new OAuthMessage(request.getMethod(), request.getRequestURL(), params);
            message.addRequiredParameters(accessor);
            List<Map.Entry<String, String>> oAuthParams = message.getParameters();
            String url = OAuth.addParameters(request.getRequestURL(), oAuthParams);
            HttpPost post = new HttpPost(url);
            File photoFile = new File(tempImagePath);
            FileBody photoContentBody = new FileBody(photoFile);
            StringBody eventIdBody = new StringBody(eventId);
            HttpClient client = new DefaultHttpClient();
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.STRICT);
            reqEntity.addPart(Constants.PHOTO, photoContentBody);
            reqEntity.addPart(Constants.EVENT_ID_KEY, eventIdBody);
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            responseString = EntityUtils.toString(resEntity);
        } catch (Exception e) {
            Log.e("OAuthConnectionManager", "Exception in uploadPhoto()", e);
            throw new ApplicationException(e);
        }
        return responseString;
    }
