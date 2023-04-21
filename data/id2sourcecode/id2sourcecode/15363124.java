    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayLinksId) {
        try {
            log.info("Eliminando ANUNCIOS: " + sArrayLinksId);
            String sSql = Statements.DELETE_MASIVE_LINKS;
            sSql = sSql.replaceFirst("v1", sArrayLinksId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de publicacaciones eliminadas => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("links.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la publicacion: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("links.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
