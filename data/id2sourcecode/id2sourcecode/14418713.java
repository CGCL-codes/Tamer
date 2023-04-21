    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayDictionaryId) {
        try {
            log.info("Eliminando ANUNCIOS: " + sArrayDictionaryId);
            String sSql = Statements.DELETE_MASIVE_DICTIONARY;
            sSql = sSql.replaceFirst("v1", sArrayDictionaryId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de publicacaciones eliminadas => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("dictionary.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la publicacion: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("dictionary.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
