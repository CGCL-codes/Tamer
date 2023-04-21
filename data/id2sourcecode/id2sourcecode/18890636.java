    protected ChannelFuture onRemoteConnected(ChannelFuture future, HTTPRequestEvent req) {
        if (logger.isDebugEnabled()) {
            logger.debug("Session[" + getID() + "] onRemoteConnected");
        }
        if (!future.isSuccess()) {
            closeLocalChannel();
            return future;
        }
        if (req.method.equalsIgnoreCase("Connect")) {
            String msg = "HTTP/1.1 200 Connection established\r\n" + "Connection: Keep-Alive\r\n" + "Proxy-Connection: Keep-Alive\r\n\r\n";
            future.getChannel().getPipeline().remove("decoder");
            if (null != localChannel && localChannel.isConnected()) {
                removeCodecHandler(localChannel);
                localChannel.write(ChannelBuffers.wrappedBuffer(msg.getBytes()));
            } else {
                future.getChannel().close();
            }
            return future;
        } else {
            ChannelBuffer msg = buildRequestChannelBuffer(req);
            if (logger.isDebugEnabled()) {
                logger.debug("Direct session[" + getID() + "] send request:\n" + msg.toString(Charset.forName("UTF-8")));
            }
            unansweredRequestCount.incrementAndGet();
            DirectRemoteChannelResponseHandler handler = future.getChannel().getPipeline().get(DirectRemoteChannelResponseHandler.class);
            handler.unanwsered = true;
            return future.getChannel().write(msg);
        }
    }
