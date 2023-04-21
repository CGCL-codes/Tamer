    private void writeResponse(MessageEvent e) {
        ChannelBuffer buf = ChannelBuffers.copiedBuffer(responseContent.toString(), GgStringUtils.UTF8);
        responseContent.setLength(0);
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION)) || (!keepAlive);
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
        response.setContent(buf);
        if (isCurrentRequestXml) {
            response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/xml");
        } else {
            response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html");
        }
        if (keepAlive) {
            response.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        if (!close) {
            response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
        }
        String cookieString = request.getHeader(HttpHeaders.Names.COOKIE);
        if (cookieString != null) {
            CookieDecoder cookieDecoder = new CookieDecoder();
            Set<Cookie> cookies = cookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                CookieEncoder cookieEncoder = new CookieEncoder(true);
                for (Cookie cookie : cookies) {
                    cookieEncoder.addCookie(cookie);
                }
                response.addHeader(HttpHeaders.Names.SET_COOKIE, cookieEncoder.encode());
            }
        }
        ChannelFuture future = e.getChannel().write(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        if (this.isPrivateDbSession && dbSession != null) {
            dbSession.disconnect();
            DbAdmin.nbHttpSession--;
            dbSession = null;
        }
    }
