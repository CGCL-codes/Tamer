    @Override
    public void onProxyEventFailed(HttpProxyEventService service, HttpResponse res, HttpProxyEvent event) {
        String proxy = (String) ownFactory.csl.invoke("reselectProxyWhenFailed", new Object[] { res, delegateFactory.getName() });
        if (null == proxy) {
            event.getChannel().write(res).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        selectProxy(proxy, event);
    }
