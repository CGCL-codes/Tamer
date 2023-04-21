    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost: from %s:%d", request.getRemoteAddr(), request.getRemotePort());
        Gravity gravity = GravityManager.getGravity(getServletContext());
        try {
            initializeRequest(gravity, request, response);
            AsyncMessage connect = getConnectMessage(request);
            if (connect != null) {
                String channelId = (String) connect.getClientId();
                ContinuationChannel channel = (ContinuationChannel) gravity.getChannel(channelId);
                synchronized (channel) {
                    channel.reset();
                    channel.runReceived(new AsyncHttpContext(request, response, connect));
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
                    ContinuationChannel channel = (ContinuationChannel) gravity.getChannel(channelId);
                    if (channel == null) throw new NullPointerException("No channel on tunnel connect");
                    if (!channel.runReceived(new AsyncHttpContext(request, response, amf3Request))) {
                        setConnectMessage(request, amf3Request);
                        synchronized (channel) {
                            Continuation continuation = ContinuationSupport.getContinuation(request, channel);
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
        } catch (RetryRequest e) {
            throw e;
        } catch (IOException e) {
            log.error(e, "Gravity message error");
            throw e;
        } catch (Exception e) {
            log.error(e, "Gravity message error");
            throw new ServletException(e);
        } finally {
            cleanupRequest(request);
        }
    }
