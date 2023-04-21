    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!request.isAsyncSupported()) throw new ServletException("Asynchronous requests are not supported with this servlet. Please check your web.xml");
        if (request.isAsyncStarted()) throw new ServletException("Gravity Servlet3 implementation doesn't support dispatch(...) mode");
        Gravity gravity = GravityManager.getGravity(getServletContext());
        try {
            initializeRequest(gravity, request, response);
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
                    if (amf3Requests.length > 1) throw new IllegalArgumentException("Only one connect request is allowed on tunnel.");
                    AsyncChannel channel = (AsyncChannel) gravity.getChannel(channelId);
                    if (channel == null) throw new NullPointerException("No channel on tunnel connect");
                    if (!channel.runReceived(new AsyncHttpContext(request, response, amf3Request))) {
                        setConnectMessage(request, amf3Request);
                        AsyncContext asyncContext = request.startAsync();
                        asyncContext.setTimeout(getLongPollingTimeout());
                        try {
                            asyncContext.addListener(new AsyncRequestListener(channel));
                            channel.setAsyncContext(asyncContext);
                        } catch (Exception e) {
                            log.error(e, "Error while setting async context. Closing context...");
                            asyncContext.complete();
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
        } catch (Exception e) {
            log.error(e, "Gravity message error");
            throw new ServletException(e);
        } finally {
            cleanupRequest(request);
        }
    }
