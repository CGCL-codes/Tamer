    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayUserId) {
        try {
            String sSql = Statements.DELETE_MASIVE_USER;
            sSql = sSql.replaceFirst("v1", sArrayUserId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" # Usuarios eliminados => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("user.deleteMasive.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la secuencia: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("user.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
