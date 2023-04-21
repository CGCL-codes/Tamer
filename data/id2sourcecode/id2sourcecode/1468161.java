    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html");
        responseContent.setLength(0);
        startHeader();
        endHeader();
        responseContent.append("OpenR66 Web Failure: ");
        responseContent.append(status.toString());
        endBody();
        response.setContent(ChannelBuffers.copiedBuffer(responseContent.toString(), "UTF-8"));
        ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }
