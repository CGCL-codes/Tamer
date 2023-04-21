    @Override
    public boolean handleError(CometEvent event) throws IOException {
        if (EventUtil.isErrorButNotTimeout(event)) log.warn("Got an error event: %s", EventUtil.toString(event));
        try {
            HttpServletRequest request = event.getHttpServletRequest();
            Message connect = getConnectMessage(request);
            if (connect != null) {
                Gravity gravity = GravityManager.getGravity(getServletContext());
                String channelId = (String) connect.getClientId();
                TomcatChannel channel = (TomcatChannel) gravity.getChannel(channelId);
                if (channel != null) channel.setCometEvent(null);
            }
        } catch (Exception e) {
            log.error(e, "Error while processing event: %s", EventUtil.toString(event));
        }
        return true;
    }
