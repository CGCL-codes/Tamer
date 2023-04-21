    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        if (req.getMethod() != GET) {
            sendHttpResponse(ctx, req, new DefaultHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }
        if (req.getUri().equals("/")) {
            HttpResponse res = new DefaultHttpResponse(HTTP_1_1, OK);
            ChannelBuffer content = WebSocketServerIndexPage.getContent(getWebSocketLocation(req));
            res.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8");
            setContentLength(res, content.readableBytes());
            res.setContent(content);
            sendHttpResponse(ctx, req, res);
            return;
        }
        if (req.getUri().equals(WEBSOCKET_PATH) && Values.UPGRADE.equalsIgnoreCase(req.getHeader(CONNECTION)) && WEBSOCKET.equalsIgnoreCase(req.getHeader(Names.UPGRADE))) {
            HttpResponse res = new DefaultHttpResponse(HTTP_1_1, new HttpResponseStatus(101, "Web Socket Protocol Handshake"));
            res.addHeader(Names.UPGRADE, WEBSOCKET);
            res.addHeader(CONNECTION, Values.UPGRADE);
            if (req.containsHeader(SEC_WEBSOCKET_KEY1) && req.containsHeader(SEC_WEBSOCKET_KEY2)) {
                res.addHeader(SEC_WEBSOCKET_ORIGIN, req.getHeader(ORIGIN));
                res.addHeader(SEC_WEBSOCKET_LOCATION, getWebSocketLocation(req));
                String protocol = req.getHeader(SEC_WEBSOCKET_PROTOCOL);
                if (protocol != null) {
                    res.addHeader(SEC_WEBSOCKET_PROTOCOL, protocol);
                }
                String key1 = req.getHeader(SEC_WEBSOCKET_KEY1);
                String key2 = req.getHeader(SEC_WEBSOCKET_KEY2);
                int a = (int) (Long.parseLong(key1.replaceAll("[^0-9]", "")) / key1.replaceAll("[^ ]", "").length());
                int b = (int) (Long.parseLong(key2.replaceAll("[^0-9]", "")) / key2.replaceAll("[^ ]", "").length());
                long c = req.getContent().readLong();
                ChannelBuffer input = ChannelBuffers.buffer(16);
                input.writeInt(a);
                input.writeInt(b);
                input.writeLong(c);
                ChannelBuffer output = ChannelBuffers.wrappedBuffer(MessageDigest.getInstance("MD5").digest(input.array()));
                res.setContent(output);
            } else {
                res.addHeader(WEBSOCKET_ORIGIN, req.getHeader(ORIGIN));
                res.addHeader(WEBSOCKET_LOCATION, getWebSocketLocation(req));
                String protocol = req.getHeader(WEBSOCKET_PROTOCOL);
                if (protocol != null) {
                    res.addHeader(WEBSOCKET_PROTOCOL, protocol);
                }
            }
            ChannelPipeline p = ctx.getChannel().getPipeline();
            p.remove("aggregator");
            p.replace("decoder", "wsdecoder", new WebSocketFrameDecoder());
            ctx.getChannel().write(res);
            p.replace("encoder", "wsencoder", new WebSocketFrameEncoder());
            return;
        }
        sendHttpResponse(ctx, req, new DefaultHttpResponse(HTTP_1_1, FORBIDDEN));
    }
