    public RestServiceResult delete(RestServiceResult serviceResult, CoMaterial coMaterial) {
        try {
            log.info("Eliminando la menu: " + coMaterial.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_MATERIAL);
            query.setParameter(1, coMaterial.getMaterialId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coMaterial.getTitle() };
            log.info("Secuencia eliminada con �xito: " + coMaterial.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el material: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coMaterial.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
