    public RestServiceResult delete(RestServiceResult serviceResult, CoUnit coUnit) {
        try {
            log.info("Eliminando la unidad: " + coUnit.getUnitName());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_UNIT);
            query.setParameter(1, coUnit.getUnitId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coUnit.getUnitName() };
            log.info("Unidad eliminada con �xito: " + coUnit.getUnitName());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("unit.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la unidad: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coUnit.getUnitName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("unit.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
