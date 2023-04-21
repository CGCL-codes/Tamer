    public void add(final boolean newsnose, final ArticleObject foundArticle, NewCanalNode node) {
        urlToArticleVector.put(foundArticle.getCaption(), foundArticle);
        final NewCanalNode cn = new NewCanalNode(foundArticle.getCaption(), CONSTANTS.TYPE_HIT, foundArticle.getUrl());
        Date date = foundArticle.getDateFound();
        if (date != null) {
            cn.setDate(date.getTime());
        } else {
            cn.setDate(System.currentTimeMillis());
        }
        cn.setXmlsource(foundArticle.getChannelURL());
        String lineage = (String) BigBlogZooView.urlSet.get(foundArticle.getChannelURL());
        cn.setLineage(lineage);
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                SearchView.addNode(newsnose, cn);
            }
        });
    }
