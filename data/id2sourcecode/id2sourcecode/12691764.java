    public RestServiceResult delete(RestServiceResult serviceResult, CoExercises1 coExercises1) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_EXERCITE1);
            query.setParameter(1, coExercises1.getExerciseId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coExercises1);
            Object[] arrayParam = { coExercises1.getExerciseName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercises1.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el ejercicio s1: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coExercises1.getExerciseName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercises1.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
