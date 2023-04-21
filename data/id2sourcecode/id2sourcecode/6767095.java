    @Override
    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
        if (arg1.equals(TAG_CATEGORY)) {
            item.setCategory(getData());
        } else if (arg1.equals(TAG_DESCRIPTION)) {
            if (getChannelTag()) {
                channel.setDescription(getData());
            } else if (getItemTag()) {
                item.setDescription(getData());
            }
        } else if (arg1.equals(TAG_GENERATOR)) {
            channel.setGenerator(getData());
        } else if (arg1.equals(TAG_LANGUAGE)) {
            channel.setLanguage(getData());
        } else if (arg1.equals(TAG_LAST_BUILD_DATE)) {
            channel.setLastBuildDate(getData());
        } else if (arg1.equals(TAG_LINK)) {
            if (getChannelTag()) {
                channel.setLink(getData());
            } else if (getItemTag()) {
                item.setLink(getData());
            }
        } else if (arg1.equals(TAG_MANAGING_EDITOR)) {
            channel.setManagingEditor(getData());
        } else if (arg1.equals(TAG_PUB_DATE)) {
            if (getChannelTag()) {
                channel.setPubDate(getData());
            } else if (getItemTag()) {
                item.setPubDate(getData());
            }
        } else if (arg1.equals(TAG_TITLE)) {
            if (getChannelTag()) {
                channel.setTitle(getData());
            } else if (getItemTag()) {
                item.setTitle(getData());
            }
        } else if (arg1.equals(TAG_WEB_MASTER)) {
            channel.setWebMaster(getData());
        } else if (arg1.equals(TAG_RSS)) {
        } else if (arg1.equals(TAG_CHANNEL)) {
        } else if (arg1.equals(TAG_ITEM)) {
        } else {
            System.out.println("WARNING@TorrentJEDI:RSSHandler endElement [" + arg1 + "] [ignored tag]");
        }
        setData("");
        setLevel(getLevel() - 1);
    }
