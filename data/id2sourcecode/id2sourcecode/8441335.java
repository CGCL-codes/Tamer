    public ModelAndView saveZappedItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Item item = getActiveItem(request);
        Stack deletedItems = getDeletedTags(request);
        HtmlContentParser contentParser = new HtmlContentParser();
        contentParser.setAddZapMethod(false);
        String content = contentParser.getContent(item.getDescription(), deletedItems);
        item.setDescription(content);
        channelController.update(channelController.getChannel(item.getChannelID()), item);
        ModelAndView modelAndView = new ModelAndView("");
        modelAndView.addObject("message", "item.saved");
        return modelAndView;
    }
