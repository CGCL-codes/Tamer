    public ModelAndView getItemAsHTML(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Item item = channelController.getItem(new Long(RequestUtils.getRequiredLongParameter(request, WorkflowConstants.ID_NAME)));
        Channel channel = channelController.getChannel(item.getChannelID());
        Item itemToFetch = new Item();
        itemToFetch.setPostedDate(item.getPostedDate());
        itemToFetch.setChannel(channel);
        itemToFetch.setUrl(item.getUrl());
        itemToFetch.getItemAsHTML();
        itemToFetch.setTitle(item.getTitle());
        itemToFetch.setFetched(true);
        channelController.update(channel, itemToFetch);
        return new ModelAndView("");
    }
