    @Override
    protected HttpProxyEvent preProcessForwardHttpProxyEvent(HttpProxyEvent event) {
        switch(event.getType()) {
            case RECV_HTTP_REQUEST:
                {
                    HttpRequest recvReq = (HttpRequest) event.getSource();
                    String host = url.getHost();
                    int port = url.getPort();
                    if (port == -1) {
                        port = 80;
                    }
                    String portstr = port != 80 ? "" + port : "";
                    HttpRequest newReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, url.toString());
                    newReq.setHeader("Host", host + portstr);
                    StringBuffer headerBuf = new StringBuffer();
                    String uri = recvReq.getUri();
                    String prefix = "http://" + recvReq.getHeader(HttpHeaders.Names.HOST);
                    if (uri.startsWith(prefix)) {
                        uri = uri.substring(prefix.length());
                    }
                    headerBuf.append(recvReq.getMethod().toString()).append(" ").append(uri).append(" ").append(recvReq.getProtocolVersion().toString()).append("\r\n");
                    newReq.removeHeader("Proxy-Connection");
                    newReq.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/octet-stream");
                    newReq.addHeader("TunnelTarget", encrpt.encrypt(getRemoteAddress(recvReq)));
                    recvReq.setHeader("Connection", "close");
                    recvReq.setHeader("Proxy-Connection", "close");
                    ChannelBuffer buffer = null;
                    try {
                        HttpRequestEncoder encoder = new HttpRequestEncoder();
                        Method m = HttpMessageEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Channel.class, Object.class);
                        m.setAccessible(true);
                        buffer = (ChannelBuffer) m.invoke(encoder, null, event.getChannel(), recvReq);
                    } catch (Exception e) {
                        logger.error("failed to encode original request", e);
                    }
                    newReq.setHeader("Content-Length", "" + buffer.readableBytes());
                    ChannelBuffer content = buffer;
                    if (logger.isDebugEnabled()) {
                        logger.debug("sending request\n" + headerBuf.toString());
                    }
                    newReq.setContent(encrypt(content));
                    event.setSource(newReq);
                    break;
                }
            case RECV_HTTP_CHUNK:
                {
                    HttpChunk chunk = (HttpChunk) event.getSource();
                    ChannelBuffer content = encrypt(chunk.getContent());
                    chunk = new DefaultHttpChunk(content);
                    event.setSource(chunk);
                    break;
                }
            default:
                return event;
        }
        return event;
    }
