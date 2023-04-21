    public synchronized void publishItem(ItemIF item) {
        if (CommonUtils.isRecentItem(item, 30) == false) {
            return;
        }
        String url = bitly.call(Bitly.shorten(item.getLink().toString())).getShortUrl();
        String message = item.getTitle() + " [" + item.getChannel().getTitle() + "]\n" + url + "  #javarss";
        publish(message);
    }
