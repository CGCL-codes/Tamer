    private MSResponse process(HttpRequestBase httpRequest) {
        MSResponse msResponse = new MSResponse();
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            StatusLine statusLine = httpResponse.getStatusLine();
            HttpEntity entity = httpResponse.getEntity();
            String response = "";
            if (entity != null) {
                response = getResponseFromStream(entity.getContent());
            }
            switch(statusLine.getStatusCode()) {
                case 200:
                case 201:
                    Object result = MSResponse.parse(response, this.mDataMapper);
                    msResponse.setSuccess(true);
                    msResponse.setResult(result);
                    break;
                default:
                    MSRequestException e;
                    String headerName = "x-opensocial-error";
                    if (httpResponse.containsHeader(headerName)) {
                        Header header = httpResponse.getFirstHeader(headerName);
                        String headerValue = header.getValue();
                        String errorMessage = statusLine.getReasonPhrase() + " : " + headerName + " : " + headerValue;
                        e = new MSRequestException(statusLine.getStatusCode(), errorMessage);
                    } else {
                        e = MSResponse.getError(response);
                    }
                    msResponse.setSuccess(false);
                    msResponse.setError(e);
                    break;
            }
        } catch (Exception e) {
            msResponse.setSuccess(false);
            msResponse.setError(e);
        }
        return msResponse;
    }
