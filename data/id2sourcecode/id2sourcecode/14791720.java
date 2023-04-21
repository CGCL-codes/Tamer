    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayThemesId) {
        try {
            log.info("Eliminando TEMAS: " + sArrayThemesId);
            String sSql = Statements.DELETE_MASIVE_THEME;
            sSql = sSql.replaceFirst("v1", sArrayThemesId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de Temas eliminados => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("theme.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el tema: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("theme.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
