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
                    originalProxyEvent = event;
                    HttpRequest request = (HttpRequest) event.getSource();
                    if (event.getType().equals(HttpProxyEventType.RECV_HTTPS_REQUEST)) {
                        isHttps = true;
                    }
                    try {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Send proxy request");
                            logger.debug(request.toString());
                        }
                        getRemoteChannel(request);
                        remoteChannel.write(request);
                    } catch (InterruptedException e) {
                        logger.error("Failed to create remote channel!", e);
                        closeChannel(localChannel);
                    }
                    break;
                }
            case RECV_HTTP_CHUNK:
            case RECV_HTTPS_CHUNK:
                {
                    remoteChannel.write(event.getSource());
                    break;
                }
        }
    }
