    public boolean resourceExists(String resourceName) throws TransferFailedException, AuthorizationException {
        HttpURLConnection headConnection;
        try {
            URL url = new URL(buildUrl(new Resource(resourceName).getName()));
            headConnection = (HttpURLConnection) url.openConnection();
            addHeaders(headConnection);
            headConnection.setRequestMethod("HEAD");
            headConnection.setDoOutput(true);
            int statusCode = headConnection.getResponseCode();
            switch(statusCode) {
                case HttpURLConnection.HTTP_OK:
                    return true;
                case HttpURLConnection.HTTP_FORBIDDEN:
                    throw new AuthorizationException("Access denied to: " + url);
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return false;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    throw new AuthorizationException("Access denied to: " + url);
                default:
                    throw new TransferFailedException("Failed to look for file: " + buildUrl(resourceName) + ". Return code is: " + statusCode);
            }
        } catch (IOException e) {
            throw new TransferFailedException("Error transferring file: " + e.getMessage(), e);
        }
    }
