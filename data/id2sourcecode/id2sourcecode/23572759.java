    @Override
    public Article deleteById(Serializable id) {
        Article entity = findById(id);
        for (CmsTopic topic : entity.getTopics()) {
            topic.getArticles().remove(entity);
        }
        CmsChannel chnl = entity.getChannel();
        removeSideArticle(entity);
        delete(entity);
        chnl.setDocCount(chnl.getDocCount() - 1);
        CmsCommentMng.deleteComment(entity.getId(), CmsComment.DOC_ARTICLE);
        removeAttachment(entity, true);
        entity.deleteContentFile(contextPvd.getAppRoot());
        return entity;
    }
