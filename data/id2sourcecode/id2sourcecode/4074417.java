    @Override
    public HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
        initialize();
        addGoogleAuthHeader(request, authToken);
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == 401) {
            Log.i(LOG_TAG, "Invalid token: " + authToken);
            accountManager.invalidateAuthToken(ACCOUNT_TYPE, authToken);
            authToken = getAuthToken();
            removeGoogleAuthHeaders(request);
            addGoogleAuthHeader(request, authToken);
            Log.i(LOG_TAG, "new token: " + authToken);
            response = client.execute(request);
        }
        return response;
    }
