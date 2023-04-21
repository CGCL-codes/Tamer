    public RestServiceResult delete(RestServiceResult serviceResult, CoMatrixExercises2 coMatrixExercises2) {
        String sExerciseName = null;
        try {
            sExerciseName = coMatrixExercises2.getCoExercises2().getExerciseName();
            log.error("Eliminando la matriz tipo 2: " + sExerciseName);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_MATRIX_EXERCISES2);
            query.setParameter(1, coMatrixExercises2.getMatrixId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sExerciseName };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("matrixExercises2.delete.success"), arrayParam));
            log.info("Eliminando matriz tipo 2: " + sExerciseName);
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar la matriz tipo 2: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coMatrixExercises2.getCoExercises2().getExerciseName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("matrixExercises2.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
