    public HttpResponse execute(HttpUriRequest request) {
        try {
            isError = false;
            if (httpclient == null) {
                isError = true;
                code = -2;
                return null;
            }
            HttpResponse response = httpclient.execute(request, localcontext);
            code = response.getStatusLine().getStatusCode();
            if (code < 200 || code >= 300) {
                isError = true;
                if (code == 401) {
                    OICSLogin.resetError();
                    closeConnection();
                    setupConnection();
                    return execute(request);
                }
            }
            return response;
        } catch (IOException e) {
            System.err.println("IOExe: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
