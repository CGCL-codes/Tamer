    public RestServiceResult delete(RestServiceResult serviceResult, ToDictionary toDictionary) {
        try {
            log.info("Eliminando la publicacion: " + toDictionary.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_DICTIONARY);
            query.setParameter(1, toDictionary.getDictionaryId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toDictionary);
            Object[] arrayParam = { toDictionary.getTitle() };
            log.info("Publicacion eliminada con �xito: " + toDictionary.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("dictionary.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la publicacion: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toDictionary.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("dictionary.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
