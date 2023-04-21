    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost: from %s:%d", request.getRemoteAddr(), request.getRemotePort());
        Gravity gravity = GravityManager.getGravity(getServletContext());
        try {
            initializeRequest(gravity, request, response);
            AsyncMessage connect = getConnectMessage(request);
            if (connect != null) {
                try {
                    String channelId = (String) connect.getClientId();
                    GenericChannel channel = (GenericChannel) gravity.getChannel(channelId);
                    synchronized (channel) {
                        channel.reset();
                        channel.runReceived(new AsyncHttpContext(request, response, connect));
                    }
                } finally {
                    removeConnectMessage(request);
                }
                return;
            }
            Message[] amf3Requests = deserialize(gravity, request);
            log.debug(">> [AMF3 REQUESTS] %s", (Object) amf3Requests);
            Message[] amf3Responses = null;
            boolean accessed = false;
            for (int i = 0; i < amf3Requests.length; i++) {
                Message amf3Request = amf3Requests[i];
                Message amf3Response = gravity.handleMessage(amf3Request);
                String channelId = (String) amf3Request.getClientId();
                if (!accessed) accessed = gravity.access(channelId);
                if (amf3Response == null) {
                    if (amf3Requests.length > 1) throw new IllegalArgumentException("Only one request is allowed on tunnel.");
                    GenericChannel channel = (GenericChannel) gravity.getChannel(channelId);
                    if (channel == null) throw new NullPointerException("No channel on tunnel connect");
                    if (!channel.runReceived(new AsyncHttpContext(request, response, amf3Request))) {
                        setConnectMessage(request, amf3Request);
                        synchronized (channel) {
                            WaitingContinuation continuation = new WaitingContinuation(channel);
                            channel.setContinuation(continuation);
                            continuation.suspend(getLongPollingTimeout());
                        }
                    }
                    return;
                }
                if (amf3Responses == null) amf3Responses = new Message[amf3Requests.length];
                amf3Responses[i] = amf3Response;
            }
            log.debug("<< [AMF3 RESPONSES] %s", (Object) amf3Responses);
            serialize(gravity, response, amf3Responses);
        } catch (IOException e) {
            log.error(e, "Gravity message error");
            throw e;
        } catch (ClassNotFoundException e) {
            log.error(e, "Gravity message error");
            throw new ServletException("Gravity message error", e);
        } finally {
            cleanupRequest(request);
        }
        removeConnectMessage(request);
    }
