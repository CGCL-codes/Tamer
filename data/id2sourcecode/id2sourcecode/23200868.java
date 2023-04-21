    @Override
    public void handleEvent(HttpProxyEvent event, HttpProxyEventCallback callback) {
        switch(event.getType()) {
            case RECV_HTTPS_REQUEST:
                {
                    originalProxyEvent = event;
                    HttpRequest req = (HttpRequest) event.getSource();
                    if (req.getMethod().equals(HttpMethod.CONNECT)) {
                        localChannel = event.getChannel();
                        try {
                            getRemoteChannel(req);
                        } catch (InterruptedException e) {
                            logger.error("", e);
                        }
                        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                        ChannelFuture future = event.getChannel().write(res);
                        removeDecoderAndEncoder(future);
                        break;
                    }
                }
            default:
                {
                    super.handleEvent(event, callback);
                    break;
                }
        }
    }
