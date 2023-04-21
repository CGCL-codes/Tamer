    public ModelAndView saveEditedItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Item item = channelController.getItem(new Long(RequestUtils.getRequiredLongParameter(request, WorkflowConstants.ID_NAME)));
        item.setDescription(request.getParameter("description"));
        channelController.update(item.getChannel(), item);
        return new ModelAndView("", "message", "message.updated");
    }
