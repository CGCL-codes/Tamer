    @Override
    public boolean handleRequest(CometEvent event, InputStream content) throws IOException, ServletException {
        Gravity gravity = GravityManager.getGravity(getServletContext());
        HttpServletRequest request = event.getHttpServletRequest();
        HttpServletResponse response = event.getHttpServletResponse();
        try {
            initializeRequest(gravity, request, response);
            Message[] amf3Requests = deserialize(gravity, request, content);
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
                    TomcatChannel channel = (TomcatChannel) gravity.getChannel(channelId);
                    if (channel == null) throw new NullPointerException("No channel on tunnel connect");
                    if (channel.runReceived(new AsyncHttpContext(request, response, amf3Request))) return true;
                    setConnectMessage(request, amf3Request);
                    channel.setCometEvent(event);
                    return false;
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
            try {
                if (content != null) content.close();
            } finally {
                cleanupRequest(request);
            }
        }
        return true;
    }
