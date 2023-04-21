    public ModelAndView setActiveChannel(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Channel channel = channelController.getChannel(new Long(RequestUtils.getRequiredLongParameter(request, WorkflowConstants.ID_NAME)));
        BindException exception = BindUtils.bind(request, channel, "channel");
        Map model = exception.getModel();
        ModelAndView view = new ModelAndView("", model);
        view.addObject("channel", channel);
        request.getSession().setAttribute(ACTIVE_CHANNEL, channel);
        return view;
    }
