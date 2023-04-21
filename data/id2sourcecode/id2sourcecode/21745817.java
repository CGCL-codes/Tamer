    protected void handleHttpRequest(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        try {
            HttpHost target = HttpUtil.getTargetHost(request, 80, "http");
            request.removeHeaders(HTTP.CONTENT_LEN);
            cleanHopByHopHeaders(request);
            HttpResponse clientResponse = httpclient.execute(target, request, context);
            cleanHopByHopHeaders(clientResponse);
            clientResponse.removeHeaders(HTTP.CONTENT_LEN);
            HttpUtil.copy(response, clientResponse);
        } catch (URISyntaxException e) {
            String message = "Error handling request to " + request.getRequestLine().getUri() + ": " + e.getMessage() + "(" + e.getClass().getName() + ")";
            LogUtils.fatal(logger, message);
            throw new HttpException(message);
        }
    }
