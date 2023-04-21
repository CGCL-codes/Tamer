    public void init() {
        System.setProperty("sun.java2d.ddscale", "true");
        RBNBController rbnbController = RBNBController.getInstance();
        String hostName = getParameter("host");
        String portString = getParameter("port");
        String channelsString = getParameter("channels");
        String playbackRateString = getParameter("playback-rate");
        String timeScaleString = getParameter("time-scale");
        boolean play = Boolean.parseBoolean(getParameter("play"));
        boolean realTime = Boolean.parseBoolean(getParameter("real-time"));
        if (playbackRateString != null && !playbackRateString.equals("")) {
            double playbackRate = Double.parseDouble(playbackRateString);
            rbnbController.setPlaybackRate(playbackRate);
        }
        if (timeScaleString != null && !timeScaleString.equals("")) {
            double timeScale = Double.parseDouble(timeScaleString);
            rbnbController.setTimeScale(timeScale);
        }
        if (portString != null && !portString.equals("")) {
            rbnbController.setRBNBPortNumber(Integer.parseInt(portString));
        }
        String[] channels = null;
        if (channelsString != null && !channelsString.equals("")) {
            channels = channelsString.split(CHANNEL_SPLIT_CHAR);
        }
        if (hostName != null && !hostName.equals("")) {
            rbnbController.setRBNBHostName(hostName);
            if (rbnbController.connect(true)) {
                if (channels != null) {
                    for (int i = 0; i < channels.length; i++) {
                        String channel = channels[i];
                        System.out.println("Viewing channel " + channel + ".");
                        org.rdv.rbnb.Channel channelTest = rbnbController.getChannel(channel);
                        if (channelTest == null) System.out.println("No such channel: " + channel);
                        DataPanelManager.getInstance().viewChannel(channel);
                    }
                }
                if (play) {
                    System.out.println("Starting data playback.");
                    rbnbController.play();
                } else if (realTime) {
                    System.out.println("Viewing data in real time.");
                    rbnbController.monitor();
                }
            }
        }
    }
