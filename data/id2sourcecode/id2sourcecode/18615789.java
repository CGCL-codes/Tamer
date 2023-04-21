    public void setCategories(Item item, List choosenCategories) {
        Channel channel = item.getChannel();
        List channelCategories = channel.getCategories();
        item.setCategories(choosenCategories);
        int nrOfPreviousCategories = channelCategories.size();
        for (int i = 0; i < choosenCategories.size(); i++) {
            Category category = (Category) choosenCategories.get(i);
            boolean foundChannel = false;
            for (int j = 0; j < nrOfPreviousCategories; j++) {
                Category savedCategory = (Category) channelCategories.get(j);
                if (savedCategory.getId().equals(category.getId())) {
                    foundChannel = true;
                    break;
                }
            }
            if (!foundChannel) {
                channelCategories.add(category);
            }
        }
        if (nrOfPreviousCategories != channelCategories.size()) {
            channel.setCategories(channelCategories);
        }
        this.update(channel, item);
    }
