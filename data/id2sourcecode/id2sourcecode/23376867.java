    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayForumId) {
        try {
            log.info("Eliminando FOROS: " + sArrayForumId);
            String sSql = Statements.DELETE_MASIVE_FORUM;
            sSql = sSql.replaceFirst("v1", sArrayForumId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de FOROS eliminados => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("forum.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el foro: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("forum.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
