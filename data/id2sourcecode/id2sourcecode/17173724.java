    public void removeItems() {
        Config config = JReader.getConfig();
        if (config.getDeleteOlderThanDays() > 0) {
            Calendar currentDate = Calendar.getInstance();
            Calendar itemDate = Calendar.getInstance();
            List<Item> removeItems = new ArrayList<Item>();
            for (Item item : this.items.values()) {
                itemDate.setTime(item.getCreationDate());
                itemDate.set(Calendar.DATE, itemDate.get(Calendar.DATE) + config.getDeleteOlderThanDays());
                if (currentDate.compareTo(itemDate) > 0) {
                    if (item.isRead()) {
                        removeItems.add(item);
                    }
                }
            }
            for (Item item : removeItems) {
                try {
                    this.channels.get(item.getChannelId()).removeItem(item.getId());
                    this.items.remove(item.getId());
                } catch (NullPointerException e) {
                    continue;
                }
            }
        }
    }
