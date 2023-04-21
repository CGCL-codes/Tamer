    protected void handleHttpsRequest(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        try {
            HttpHost target = HttpUtil.getTargetHost(request, 443, "https");
            cleanHopByHopHeaders(request);
            request.removeHeaders(HTTP.CONTENT_LEN);
            HttpResponse clientResponse = ((ShibbolethHttpClient) httpclient).executeShibboleth(target, request);
            cleanHopByHopHeaders(clientResponse);
            clientResponse.removeHeaders(HTTP.CONTENT_LEN);
            HttpUtil.copy(response, clientResponse);
        } catch (URISyntaxException e) {
            String message = "Error handling request to " + request.getRequestLine().getUri() + ": " + e.getMessage() + "(" + e.getClass().getName() + ")";
            LogUtils.fatal(logger, message);
            throw new HttpException(message);
        } catch (InterruptedException e) {
            String message = "Error handling request to " + request.getRequestLine().getUri() + ": " + e.getMessage() + "(" + e.getClass().getName() + ")";
            LogUtils.fatal(logger, message);
            throw new HttpException(message);
        }
    }
