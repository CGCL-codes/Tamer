    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayNewsId) {
        try {
            log.info("Eliminando ANUNCIOS: " + sArrayNewsId);
            String sSql = Statements.DELETE_MASIVE_NEWS;
            sSql = sSql.replaceFirst("v1", sArrayNewsId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de ANUNCIOS eliminados => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("news.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el anuncio: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("news.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
