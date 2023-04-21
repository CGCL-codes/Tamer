    private void setupFlexTPSStream() {
        String channelName = (String) channels.iterator().next();
        Channel channel = rbnbController.getChannel(channelName);
        if (channel == null) {
            return;
        }
        String host = channel.getMetadata("flexTPS_host");
        String feed = channel.getMetadata("flexTPS_feed");
        String stream = channel.getMetadata("flexTPS_stream");
        Authentication auth = AuthenticationManager.getInstance().getAuthentication();
        String gaSession = null;
        if (auth != null) {
            gaSession = auth.get("session");
        }
        if (host != null && feed != null && stream != null) {
            flexTPSStream = new FlexTPSStream(host, feed, stream, gaSession);
            if (flexTPSStream.canDoRobotic()) {
                setRoboticControls();
            } else {
                flexTPSStream = null;
                removeRoboticControls();
            }
        }
    }
