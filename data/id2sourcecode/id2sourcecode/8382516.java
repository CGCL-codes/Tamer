    public void acceptChanges() {
        settings.setTreeFont(TreeFont);
        settings.setArticleFont(ArticleFont);
        settings.setTableFont(TableFont);
        Container parent = this;
        while (parent.getParent() != null) parent = parent.getParent();
        (((RssView) parent).getChannelList()).repaintTree();
        (((RssView) parent).getChannelTitle()).repaintTable();
    }
