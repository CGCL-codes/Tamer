    protected ChannelFuture onRemoteConnected(ChannelFuture future, HTTPRequestEvent req) {
        {
            logger.info("Session[" + getID() + "] onRemoteConnected with Host:" + req.getHeader("Host"));
        }
        if (!future.isSuccess()) {
            logger.error("Session[" + getID() + "] close local connection since connect failed.");
            closeLocalChannel();
            return future;
        }
        if (req.method.equalsIgnoreCase("Connect")) {
            HTTPResponseEvent response = new HTTPResponseEvent();
            response.setHash(getID());
            response.statusCode = 200;
            response.addHeader("Connection", "Keep-Alive");
            response.addHeader("Proxy-Connection", "Keep-Alive");
            future.getChannel().getPipeline().remove("decoder");
            EventService.getInstance(sessionManager.getUserToken()).offer(response, future.getChannel());
            return future;
        } else {
            ChannelBuffer msg = buildRequestChannelBuffer(req);
            if (logger.isDebugEnabled()) {
                logger.debug("Direct session[" + getID() + "] send request:\n" + msg.toString(Charset.forName("UTF-8")));
            }
            DirectRemoteChannelResponseHandler handler = future.getChannel().getPipeline().get(DirectRemoteChannelResponseHandler.class);
            handler.unanwsered = true;
            return future.getChannel().write(msg);
        }
    }
