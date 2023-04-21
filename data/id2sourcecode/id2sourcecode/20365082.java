    @Override
    public void handleEvent(final HttpProxyEvent event, HttpProxyEventCallback callback) {
        this.callback = callback;
        if (logger.isDebugEnabled()) {
            logger.debug("Handle event:" + event.getType() + " in handler:" + hashCode());
        }
        try {
            switch(event.getType()) {
                case RECV_HTTP_REQUEST:
                case RECV_HTTPS_REQUEST:
                    {
                        this.channel = event.getChannel();
                        HttpRequest request = (HttpRequest) event.getSource();
                        proxyHttpVer = request.getProtocolVersion();
                        this.originalProxyEvent = event;
                        ishttps = event.getType().equals(HttpProxyEventType.RECV_HTTPS_REQUEST);
                        if (request.getMethod().equals(HttpMethod.CONNECT)) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Recv https Connect request:" + request);
                            }
                            httpspath = request.getHeader("Host");
                            if (httpspath == null) {
                                httpspath = request.getUri();
                            }
                            String httpshost = httpspath;
                            String httpsport = "443";
                            if (httpspath.indexOf(":") != -1) {
                                httpshost = httpspath.substring(0, httpspath.indexOf(":"));
                                httpsport = httpspath.substring(httpspath.indexOf(":") + 1);
                            }
                            sslContext = ClientUtils.getFakeSSLContext(httpshost, httpsport);
                            HttpResponse response = new DefaultHttpResponse(proxyHttpVer, HttpResponseStatus.OK);
                            event.getChannel().write(response).addListener(new ChannelFutureListener() {

                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    if (event.getChannel().getPipeline().get("ssl") == null) {
                                        InetSocketAddress remote = (InetSocketAddress) event.getChannel().getRemoteAddress();
                                        SSLEngine engine = sslContext.createSSLEngine(remote.getAddress().getHostAddress(), remote.getPort());
                                        engine.setUseClientMode(false);
                                        event.getChannel().getPipeline().addBefore("decoder", "ssl", new SslHandler(engine));
                                    }
                                }
                            });
                        } else {
                            if (null == selector) {
                                HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.SERVICE_UNAVAILABLE);
                                event.getChannel().write(res);
                                return;
                            }
                            forwardRequest = buildForwardRequest(request);
                            asyncFetch(forwardRequest);
                        }
                        break;
                    }
                case RECV_HTTP_CHUNK:
                case RECV_HTTPS_CHUNK:
                    {
                        HttpChunk chunk = (HttpChunk) event.getSource();
                        synchronized (chunkedBodys) {
                            chunkedBodys.add(chunk.getContent());
                            chunkedBodys.notify();
                        }
                        break;
                    }
            }
        } catch (Exception e) {
            logger.error("Failed to handle this event.", e);
        }
    }
