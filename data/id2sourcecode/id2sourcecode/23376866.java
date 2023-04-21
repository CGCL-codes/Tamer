    public RestServiceResult delete(RestServiceResult serviceResult, ToForum toForum) {
        try {
            log.info("Eliminando el foro: " + toForum.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_FORUM);
            query.setParameter(1, toForum.getForumId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toForum);
            Object[] arrayParam = { toForum.getTitle() };
            log.info("Anuncio eliminado con �xito: " + toForum.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("forum.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el foro: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toForum.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("forum.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
