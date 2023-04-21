    private void addSideArticle(Article entity) {
        if (!entity.getCheck() && entity.getDisabled()) {
            return;
        }
        Long webId = entity.getWebsite().getId();
        Long chnlId = entity.getChannel().getId();
        Article pre = getDao().getSideArticle(webId, chnlId, entity.getId(), false);
        if (pre != null) {
            Article next = pre.getNext();
            pre.setNext(entity);
            entity.setPre(pre);
            entity.setNext(next);
        } else {
            Article next = getDao().getSideArticle(webId, chnlId, entity.getId(), true);
            if (next != null) {
                next.setPre(entity);
                entity.setNext(next);
            }
        }
    }
