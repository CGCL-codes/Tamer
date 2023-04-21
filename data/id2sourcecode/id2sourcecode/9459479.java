    private InputStream getResponseStream(HttpClient client, HttpUriRequest request) throws AuthenticationException, InvalidProfileException, ServiceException {
        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            int code = response.getStatusLine().getStatusCode();
            String message = response.getStatusLine().getReasonPhrase();
            switch(code) {
                case 200:
                case 201:
                    break;
                case 401:
                    throw new AuthenticationException(code, message, bufferData(entity.getContent()));
                case 403:
                    throw new InvalidProfileException();
                case 404:
                default:
                    throw new ServiceException(code, message, bufferData(entity.getContent()));
            }
            return entity.getContent();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
