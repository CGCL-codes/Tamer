    public ModelAndView pollRedirected(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        List channels = channelController.getChannels("redirectQueries");
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = (Channel) channels.get(i);
            channelController.pollChannel(channel);
        }
        return new ModelAndView("");
    }
