    public boolean addFeedItem(String titleChannel, String titleItem, String link, String description, Date date) {
        FSAChannel ch = (FSAChannel) getChannels().get(titleChannel);
        if (ch == null) {
            ch = createDeviceChannel(titleChannel, link, description);
        }
        if (ch != null) {
            try {
                if (ch.getItems().size() >= ch.getMaxNumberOfItems()) {
                    deleteLastItem(ch);
                }
                ItemIF item = new Item();
                item.setTitle(titleItem + " <date>" + date.toString() + "</date>");
                item.setCreator("FSA");
                item.setDescription(description);
                if (!link.contentEquals("")) {
                    item.setLink(new URL(link));
                }
                item.setDate(date);
                ItemGuid guid = new ItemGuid(item);
                guid.setId(date.getTime());
                guid.setLocation(date.toString());
                item.setGuid(guid);
                ch.addItem(item);
                ch.setPubDate(date);
                createRSSXML(ch);
                return true;
            } catch (MalformedURLException ex) {
                Logger.getLogger(RSS.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
