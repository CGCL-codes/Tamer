    public boolean addFeedItem(String titleChannel, String titleItem, String link, String description, Date date, String imageURL, String imageAlt) {
        FSAChannel ch = (FSAChannel) getChannels().get(titleChannel);
        if (ch != null) {
            try {
                String image = "";
                if (!imageURL.contentEquals("")) {
                    image = "<img vspace'4' hspace='4' border='1' src='" + imageURL + "' alt='" + imageAlt + "' />";
                    image = "<img hspace=\"4\" vspace=\"4\" border=\"1\" align=\"right\" alt=\"" + imageAlt + "\" src=\"" + imageURL + "\"/>";
                }
                if (ch.getItems().size() >= ch.getMaxNumberOfItems()) {
                    deleteLastItem(ch);
                }
                ItemIF item = new Item();
                item.setTitle(titleItem);
                item.setCreator("FSA");
                item.setDescription(description + " " + image);
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
