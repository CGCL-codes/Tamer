    public Item getFirstItem() {
        Channel c = this.getChannel();
        if (c != null) {
            List<Item> items = c.getItems();
            return items.get(0);
        }
        return null;
    }
