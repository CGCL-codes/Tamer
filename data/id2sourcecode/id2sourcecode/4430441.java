    private XML getChannelXML() {
        XML channel = new XML("channel");
        channel.setPrettyPrint(true);
        channel.addElement(new XML("title").addElement(getTitle()));
        channel.addElement(new XML("link").addElement(getURL()));
        channel.addElement(new XML("description").addElement(getDescription()));
        channel.addElement(new XML("language").addElement(m_feed_language));
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        for (Iterator i = m_entries.iterator(); i.hasNext(); ) {
            FeedEntry entry = (FeedEntry) i.next();
            XML item = new XML("item");
            item.addElement(new XML("title").addElement(entry.getTitle()));
            item.addElement(new XML("link").addElement(entry.getURL()));
            item.addElement(new XML("description").addElement(entry.getDescription()));
            if (entry.getCreator() != null) item.addElement(new XML("dc:creator").addElement(entry.getCreator()));
            Date modified_date = entry.getLastModifiedDate();
            if (modified_date != null) item.addElement(new XML("dc:date").addElement(date_format.format(modified_date)));
            channel.addElement(item);
        }
        return channel;
    }
