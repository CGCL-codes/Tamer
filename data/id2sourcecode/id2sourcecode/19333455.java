    protected Response invoke(Connection connection, Request request) {
        Response response;
        Multiplexer multiplexer;
        synchronized (this) {
            multiplexer = _multiplexer;
        }
        if (multiplexer != null) {
            Channel channel = null;
            try {
                channel = multiplexer.getChannel();
                response = channel.invoke(request);
                channel.release();
            } catch (Exception exception) {
                _log.debug(exception, exception);
                response = new Response(exception);
                if (channel != null) {
                    channel.destroy();
                }
            }
        } else {
            response = new Response(new ResourceException("Connection lost"));
        }
        return response;
    }
