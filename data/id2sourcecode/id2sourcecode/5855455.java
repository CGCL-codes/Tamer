    public ModelAndView indexItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List channels = this.channelController.getChannelsToPoll();
        channels.addAll(this.channelController.getArticles());
        searchIndex.createIndexWriter();
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = (Channel) channels.get(i);
            if (channel.isRemove()) {
                continue;
            }
            List items = channelController.getAllItems(channel.getId());
            for (int j = 0; j < items.size(); j++) {
                Item item = (Item) items.get(j);
                if (item.isRemove()) {
                    continue;
                }
                item.setPreferance(item.getPreferance() + channel.getPreferance());
                searchIndex.insert(new SearchItemVisitor(item));
            }
        }
        searchIndex.optimize();
        searchIndex.close();
        return new ModelAndView("");
    }
