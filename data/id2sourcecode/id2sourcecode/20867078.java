    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayDailyId) {
        try {
            log.info("Eliminando DIARIO: " + sArrayDailyId);
            String sSql = Statements.DELETE_MASIVE_NEWS;
            sSql = sSql.replaceFirst("v1", sArrayDailyId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de DIARIO eliminados => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("daily.delete.success"), arrayParam));
        } catch (Exception e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el diario: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("daily.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
