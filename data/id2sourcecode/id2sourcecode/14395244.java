    public RestServiceResult delete(RestServiceResult serviceResult, CoExercises2 coExercises2) {
        CoExercises2DAO coExercises2DAO = new CoExercises2DAO();
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_EXERCITE2);
            query.setParameter(1, coExercises2.getExerciseId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coExercises2);
            Object[] arrayParam = { coExercises2.getExerciseName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercises2.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el ejercicio s2: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coExercises2.getExerciseName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercises2.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
