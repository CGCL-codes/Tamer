    public String getChannelDate() {
        String result = "";
        if (myFeed.getPublishedDate() != null) {
            result = myFeed.getPublishedDate().toString();
        }
        return result;
    }
