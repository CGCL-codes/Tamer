    public void go() {
        HttpClient client = new DefaultHttpClient();
        Object responseData = null;
        Response response = new Response();
        response.setTag(getRequest().getTag());
        Log.i("AbstractHttpCommand", "Created the request: " + client + ", for request: " + request);
        try {
            HttpResponse rawResponse = client.execute(request);
            if (rawResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseData = getSuccessResponse(rawResponse);
                response.setError(false);
            } else {
                responseData = getErrorResponse(rawResponse);
                response.setError(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseData = getErrorResponse(e);
            response.setError(true);
        }
        response.setData(responseData);
        setResponse(response);
    }
