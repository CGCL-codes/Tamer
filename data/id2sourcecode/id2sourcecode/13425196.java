    public Enclosure getFirstEnclosure() {
        Channel c = this.getChannel();
        if (c != null) {
            List<Item> items = c.getItems();
            if (items.size() > 0) {
                Enclosure enc = items.get(0).getEnclosure();
                return enc;
            }
        }
        return null;
    }
