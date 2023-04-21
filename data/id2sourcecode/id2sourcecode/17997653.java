    private void selectProxy(String proxy, HttpProxyEvent event) {
        if (null == proxy) {
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            event.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        delegateFactory = HttpProxyEventServiceFactory.Registry.getHttpProxyEventServiceFactory(proxy);
        if (null == delegateFactory) {
            if (proxy.startsWith("socks")) {
                delegateFactory = SocksForwardProxyEventServiceFactory.getInstance();
                String[] ss = proxy.split(":");
                if (ss.length != 3) {
                    logger.error("Invalid Socks proxy setting!");
                    return;
                }
                String procol = ss[0].trim();
                String host = ss[1].trim();
                int port = Integer.parseInt(ss[2].trim());
                if (null == delegate) {
                    SocksForwardProxyEventService serv = (SocksForwardProxyEventService) delegateFactory.createHttpProxyEventService();
                    try {
                        serv.setSocksProxy(procol, host, port);
                    } catch (UnknownHostException e) {
                        logger.error("Invalid Socks proxy setting!", e);
                        return;
                    }
                    delegate = serv;
                }
            } else if (proxy.indexOf(":") != -1) {
                delegateFactory = ForwardProxyEventServiceFactory.getInstance();
                if (null == delegate) {
                    int index = proxy.indexOf(":");
                    String host = proxy.substring(0, index).trim();
                    int port = Integer.parseInt(proxy.substring(index + 1).trim());
                    ForwardProxyEventService es = (ForwardProxyEventService) delegateFactory.createHttpProxyEventService();
                    es.setRemoteAddr(host, port);
                    delegate = es;
                }
            }
        } else {
            if (null != delegate) {
                try {
                    delegate.close();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            delegate = delegateFactory.createHttpProxyEventService();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Delegate proxy to :" + delegateFactory.getName());
        }
        delegate.handleEvent(event, this);
    }
