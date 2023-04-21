    public void handleHttpRequest(ChannelHandlerContext context, HttpRequest request) {
        if (isNotHttpGetRequest(request)) {
            sendHttpResponse(context, request, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }
        try {
            HttpResponse response = processHandshake(context, request);
            if (!response.getStatus().equals(HttpResponseStatus.SWITCHING_PROTOCOLS)) {
                sendHttpResponse(context, request, response);
                return;
            }
            initializeWebsocketConnector(context, request, response);
            replaceListeners(context, request);
        } catch (NoSuchAlgorithmException e) {
            context.getChannel().close();
        }
    }
