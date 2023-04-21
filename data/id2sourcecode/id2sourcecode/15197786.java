    private HttpResponse constructHandShakeResponse(HttpRequest aReq, ChannelHandlerContext aCtx) throws NoSuchAlgorithmException {
        HttpResponse lResp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(101, "Web Socket Protocol Handshake"));
        lResp.addHeader(HttpHeaders.Names.UPGRADE, HttpHeaders.Values.WEBSOCKET);
        lResp.addHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.UPGRADE);
        if (aReq.containsHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY1) && aReq.containsHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY2)) {
            lResp.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_ORIGIN, aReq.getHeader(HttpHeaders.Names.ORIGIN));
            lResp.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_LOCATION, getWebSocketLocation(aReq));
            String lProtocol = aReq.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL);
            if (lProtocol != null) {
                lResp.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL, lProtocol);
            } else {
                lProtocol = aReq.getHeader(HttpHeaders.Names.WEBSOCKET_PROTOCOL);
                if (lProtocol != null) {
                    lResp.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL, lProtocol);
                }
            }
            String lKey1 = aReq.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY1);
            String lKey2 = aReq.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY2);
            int lA = (int) (Long.parseLong(lKey1.replaceAll("[^0-9]", "")) / lKey1.replaceAll("[^ ]", "").length());
            int lB = (int) (Long.parseLong(lKey2.replaceAll("[^0-9]", "")) / lKey2.replaceAll("[^ ]", "").length());
            long lC = aReq.getContent().readLong();
            ChannelBuffer lInput = ChannelBuffers.buffer(16);
            lInput.writeInt(lA);
            lInput.writeInt(lB);
            lInput.writeLong(lC);
            ChannelBuffer lOutput = ChannelBuffers.wrappedBuffer(MessageDigest.getInstance("MD5").digest(lInput.array()));
            lResp.setContent(lOutput);
        } else {
            lResp.addHeader(HttpHeaders.Names.WEBSOCKET_ORIGIN, aReq.getHeader(HttpHeaders.Names.ORIGIN));
            lResp.addHeader(HttpHeaders.Names.WEBSOCKET_LOCATION, getWebSocketLocation(aReq));
            String lProtocol = aReq.getHeader(HttpHeaders.Names.WEBSOCKET_PROTOCOL);
            if (lProtocol != null) {
                lResp.addHeader(HttpHeaders.Names.WEBSOCKET_PROTOCOL, lProtocol);
            }
        }
        return lResp;
    }
