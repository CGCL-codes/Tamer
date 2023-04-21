    public void setRead(int index, boolean read) {
        Article article = get(index);
        Channel parentChannel = article.getChannel();
        if (parentChannel != null) {
            parentChannel.setRead(parentChannel.indexOf(article), read);
        }
    }
