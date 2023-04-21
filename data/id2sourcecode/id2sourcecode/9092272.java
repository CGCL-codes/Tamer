    public RestServiceResult delete(RestServiceResult serviceResult, ToComment toComment) {
        try {
            log.info("Eliminando la publicacion: " + toComment.getCommentId());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_COMMENT);
            query.setParameter(1, toComment.getCommentId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toComment);
            Object[] arrayParam = { toComment.getCommentId() };
            log.info("Publicacion eliminada con �xito: " + toComment.getCommentId());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("comment.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la publicacion: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toComment.getCommentId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("comment.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
