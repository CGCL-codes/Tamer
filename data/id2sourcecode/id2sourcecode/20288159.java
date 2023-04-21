    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayForumPostId) {
        try {
            log.info("Eliminando ANUNCIO FOROS: " + sArrayForumPostId);
            String sSql = Statements.DELETE_MASIVE_NEWS;
            sSql = sSql.replaceFirst("v1", sArrayForumPostId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de ANUNCIO FOROS eliminados => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("forumPost.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el anuncio foro: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("forumPost.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
