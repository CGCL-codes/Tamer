    public ModelAndView getChannelsWithNews(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ModelAndView view = new ModelAndView("");
        view.addObject("articles", channelController.getChannelsToRead());
        request.getSession().setAttribute("current", "getChannelsWithNews");
        request.getSession().setAttribute(WorkflowConstants.RESPONSE_PAGE, request.getAttribute(WorkflowConstants.RESPONSE_PAGE));
        return view;
    }
