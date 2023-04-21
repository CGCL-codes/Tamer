    public void update(Article article) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Article old = pm.getObjectById(Article.class, article.getId());
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            old.setChannelId(article.getChannelId());
            old.setContent(article.getContent());
            old.setCreateTime(old.getCreateTime());
            old.setCreateUserId(article.getCreateUserId());
            old.setIfShow(article.getIfShow());
            old.setIsDeleted(article.getIsDeleted());
            old.setRank(article.getRank());
            old.setTitle(article.getTitle());
            old.setUpdateTime(new Date());
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            pm.close();
        }
    }
