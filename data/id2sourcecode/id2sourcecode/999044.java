    @Override
    public void handleEvent(HttpProxyEvent event, HttpProxyEventCallback callback) {
        if (logger.isDebugEnabled()) {
            logger.debug("Handle event:" + event.getType());
        }
        this.callback = callback;
        switch(event.getType()) {
            case RECV_HTTP_REQUEST:
            case RECV_HTTPS_REQUEST:
                {
                    localChannel = event.getChannel();
                    HttpRequest request = (HttpRequest) event.getSource();
                    InetSocketAddress remote = getHostHeaderAddress(request);
                    try {
                        if (null == client) {
                            client = new SocksSocket(socksproxy, remote.getHostName(), remote.getPort());
                            if (logger.isDebugEnabled()) {
                                logger.debug("Create a socks socket fore remote:" + remote);
                            }
                            Misc.getGlobalThreadPool().submit(this);
                        }
                        if (request.getMethod().equals(HttpMethod.CONNECT)) {
                            HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                            ChannelFuture future = event.getChannel().write(res);
                            removeCodecHandler(event.getChannel(), future);
                            future.awaitUninterruptibly();
                            return;
                        } else {
                            String uri = request.getUri();
                            HttpRequest newreq = request;
                            if (request.getHeader(HttpHeaders.Names.HOST) != null) {
                                String prefix = "http://" + request.getHeader(HttpHeaders.Names.HOST);
                                if (uri.startsWith(prefix)) {
                                    uri = uri.substring(prefix.length());
                                    newreq = new DefaultHttpRequest(request.getProtocolVersion(), request.getMethod(), uri);
                                    Set<String> headers = request.getHeaderNames();
                                    for (String headerName : headers) {
                                        List<String> headerValues = request.getHeaders(headerName);
                                        if (null != headerValues) {
                                            for (String headerValue : headerValues) {
                                                newreq.addHeader(headerName, headerValue);
                                            }
                                        }
                                    }
                                    newreq.setContent(request.getContent());
                                }
                            }
                            HttpRequestEncoder encoder = new HttpRequestEncoder();
                            Method m = HttpMessageEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Channel.class, Object.class);
                            m.setAccessible(true);
                            ChannelBuffer buf = (ChannelBuffer) m.invoke(encoder, null, event.getChannel(), newreq);
                            writeRemoteSocket(buf);
                        }
                    } catch (Exception e) {
                        logger.error("Failed to write remote channel!", e);
                        closeLocalChannel();
                        closeSocksClient();
                    }
                    break;
                }
            case RECV_HTTP_CHUNK:
            case RECV_HTTPS_CHUNK:
                {
                    ChannelBuffer buffer = null;
                    if (event.getSource() instanceof HttpChunk) {
                        HttpChunk chunk = (HttpChunk) event.getSource();
                        buffer = chunk.getContent();
                    } else if (event.getSource() instanceof ChannelBuffer) {
                        buffer = (ChannelBuffer) event.getSource();
                    } else {
                        logger.error("Unexpected event type:" + event.getSource().getClass());
                        closeLocalChannel();
                        closeSocksClient();
                        return;
                    }
                    try {
                        if (null != buffer) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Write " + buffer.readableBytes() + " bytes to socks proxy!");
                            }
                            writeRemoteSocket(buffer);
                        }
                    } catch (IOException e) {
                        logger.error("Failed to write remote channel!", e);
                        closeLocalChannel();
                        closeSocksClient();
                    }
                    break;
                }
        }
    }
