    public RestServiceResult delete(RestServiceResult serviceResult, ToDaily toDaily) {
        try {
            log.info("Eliminando el diario: " + toDaily.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_DAILY);
            query.setParameter(1, toDaily.getDailyId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toDaily);
            Object[] arrayParam = { toDaily.getTitle() };
            log.info("Diario eliminado con �xito: " + toDaily.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("daily.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el diario: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toDaily.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("daily.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
