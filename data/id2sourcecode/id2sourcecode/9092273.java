    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayCommentId) {
        try {
            log.info("Eliminando ANUNCIOS: " + sArrayCommentId);
            String sSql = Statements.DELETE_MASIVE_NEWS;
            sSql = sSql.replaceFirst("v1", sArrayCommentId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de publicacaciones eliminadas => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("comment.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la publicacion: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("comment.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
