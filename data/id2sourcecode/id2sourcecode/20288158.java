    public RestServiceResult delete(RestServiceResult serviceResult, ToForumPost toForumPost) {
        try {
            log.info("Eliminando el anuncio foro: " + toForumPost.getForumPostId());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_FORUM_POST);
            query.setParameter(1, toForumPost.getForumPostId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toForumPost);
            Object[] arrayParam = { toForumPost.getForumPostId() };
            log.info("Anuncio de Foro  eliminado con �xito: " + toForumPost.getForumPostId());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("forumPost.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el foro: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toForumPost.getForumPostId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("forumPost.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
