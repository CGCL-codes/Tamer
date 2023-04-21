    public RestServiceResult delete(RestServiceResult serviceResult, CoActivity coActivity) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_ACTIVITY);
            query.setParameter(1, coActivity.getActivityId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coActivity.getActivityName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("activity.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el curso: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coActivity.getActivityName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("activity.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
