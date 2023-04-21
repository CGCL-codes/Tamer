    public synchronized ConcreteElement postArticle(String provider, RunData rundata) {
        ElementContainer ec = new ElementContainer();
        ParameterParser params = rundata.getParameters();
        String topic = params.getString("topic", "");
        String title = params.getString("title", "");
        String link = params.getString("link", "");
        String description = params.getString("description", "");
        Item item = new Item();
        item.setTopic(topic);
        item.setTitle(title);
        item.setLink(link);
        item.setDescription(description);
        Content content = null;
        try {
            content = this.getContentMarkup(this.getURL(provider)).getContent();
            Vector v = new Vector();
            Item[] items = content.getChannel().getItem();
            for (int i = 0; i < items.length; ++i) {
                v.addElement(items[i]);
            }
            v.insertElementAt(item, 0);
            Item[] newItems = new Item[v.size()];
            v.copyInto(newItems);
            content.getChannel().removeAllItem();
            for (int i = 0; i < newItems.length; ++i) {
                content.getChannel().addItem(newItems[i]);
            }
            this.getContentMarkup(this.getURL(provider)).save();
        } catch (Throwable t) {
            logger.error("Throwable", t);
            return new StringElement("Can't use this provider: " + t.getMessage());
        }
        ec.addElement("Your article '" + title + "' has been posted within '" + topic + "'");
        return ec;
    }
