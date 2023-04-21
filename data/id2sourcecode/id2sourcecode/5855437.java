    public ModelAndView getChannelsWithNoNews(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ModelAndView view = new ModelAndView("");
        view.addObject("articles", channelController.getNoNewsChannels());
        request.getSession().setAttribute("current", "getChannelsWithNoNews");
        request.getSession().setAttribute(WorkflowConstants.RESPONSE_PAGE, request.getAttribute(WorkflowConstants.RESPONSE_PAGE));
        return view;
    }
