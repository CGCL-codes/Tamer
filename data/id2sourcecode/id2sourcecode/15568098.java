    @Override
    public boolean handleError(HttpEvent event) throws IOException {
        if (EventUtil.isErrorButNotTimeout(event)) log.error("Got an error event: %s", EventUtil.toString(event));
        try {
            HttpServletRequest request = event.getHttpServletRequest();
            Message connect = getConnectMessage(request);
            if (connect != null) {
                Gravity gravity = GravityManager.getGravity(getServletContext());
                String channelId = (String) connect.getClientId();
                JBossWebChannel channel = (JBossWebChannel) gravity.getChannel(channelId);
                if (channel != null) channel.setHttpEvent(null);
            }
        } catch (Exception e) {
            log.error(e, "Error while processing event: %s", EventUtil.toString(event));
        }
        return true;
    }
