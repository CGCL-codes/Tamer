    public static void splitLists(ArrayList articleList) {
        channelTitleSet.clear();
        chanTitleToArticleList.clear();
        for (Iterator iter = articleList.iterator(); iter.hasNext(); ) {
            ArticleObject article = (ArticleObject) iter.next();
            String chantitle = article.getChannelTitle();
            if (channelTitleSet.contains(chantitle)) {
                ArrayList list = (ArrayList) chanTitleToArticleList.get(chantitle);
                list.add(article);
                chanTitleToArticleList.put(chantitle, list);
            } else {
                channelTitleSet.add(chantitle);
                ArrayList list = new ArrayList();
                list.add(article);
                chanTitleToArticleList.put(chantitle, list);
                chanTitleToUrl.put(chantitle, article.getChannelURL());
            }
        }
    }
