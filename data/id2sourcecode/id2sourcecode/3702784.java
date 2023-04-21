    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        long id = ServletRequestUtils.getLongParameter(request, WorkflowConstants.ID_NAME, -1);
        Channel channel = null;
        if (id != -1) {
            channel = channelController.getChannel(new Long(id));
            request.getSession().setAttribute(CHANNEL_FOR_EDIT, channel.getId());
            if (channel == null || channel.getId() == null) {
                channel = (Channel) request.getSession().getAttribute(ChannelWebController.ACTIVE_CHANNEL);
                if (channel == null) {
                    String page = getCurrentPage(request);
                    Map model = new HashMap();
                    model.put("message", "backbutton");
                    return new ModelAndView("forward:" + page, model);
                }
            }
        } else {
            if (request.getSession().getAttribute(CHANNEL_FOR_EDIT) == null) {
                channel = new Channel();
            } else {
                channel = channelController.getChannel((Long) request.getSession().getAttribute(CHANNEL_FOR_EDIT));
            }
        }
        return channel;
    }
