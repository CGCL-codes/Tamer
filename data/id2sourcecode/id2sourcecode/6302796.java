    public void markArticlesRead() {
        for (Iterator iter = articles.iterator(); iter.hasNext(); ) {
            Article article = (Article) iter.next();
            Channel parentChannel = article.getChannel();
            if (parentChannel != null) {
                parentChannel.setRead(parentChannel.indexOf(article), true);
            }
        }
    }
