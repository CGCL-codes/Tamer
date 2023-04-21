    public ModelAndView pollChannel(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        long id = RequestUtils.getLongParameter(request, WorkflowConstants.ID_NAME, -1);
        Channel channel = null;
        if (id == -1) {
            channel = (Channel) request.getSession().getAttribute(ACTIVE_CHANNEL);
            if (channel == null) {
                ModelAndView current = getCurrent(request);
                current.addObject("message", "error.backbutton");
                return current;
            }
        } else {
            channel = channelController.getChannel(new Long(id));
        }
        channelController.pollChannel(channel);
        return new ModelAndView("");
    }
