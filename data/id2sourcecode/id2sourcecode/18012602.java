    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    if (amf3Requests.length > 1) throw new IllegalArgumentException("Only one request is allowed on tunnel.");
                    GAEChannel channel = (GAEChannel) gravity.getChannel(channelId);
                    if (channel == null) throw new NullPointerException("No channel on connect");
                    long pollingInterval = gravity.getGravityConfig().getExtra().get("gae/@polling-interval", Long.TYPE, GAE_POLLING_INTERVAL);
                    long initialTime = System.currentTimeMillis();
                    do {
                        List<Message> messages = null;
                        synchronized (channel) {
                            messages = channel.takeMessages();
                        }
                        if (messages != null) {
                            amf3Responses = messages.toArray(new Message[0]);
                            ((AsyncMessage) amf3Responses[i]).setCorrelationId(amf3Requests[i].getMessageId());
                            break;
                        }
                        try {
                            Thread.sleep(pollingInterval);
                        } catch (InterruptedException e) {
                            break;
                        } catch (DeadlineExceededException e) {
                            break;
                        }
                    } while (System.currentTimeMillis() - initialTime < GAE_TIMEOUT);
                    if (amf3Responses == null) amf3Responses = new Message[0];
                } else {
                    if (amf3Responses == null) amf3Responses = new Message[amf3Requests.length];
                    amf3Responses[i] = amf3Response;
                }
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
