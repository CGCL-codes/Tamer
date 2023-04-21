    public ModelAndView pollAllChannels(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        List allChannelsToPoll = channelController.getChannelsToPoll();
        for (int i = 0; i < allChannelsToPoll.size(); i++) {
            Channel channel = (Channel) allChannelsToPoll.get(i);
            channelController.pollChannel(channel);
        }
        return getCurrent(request);
    }
