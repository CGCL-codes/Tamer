    public RestServiceResult delete(RestServiceResult serviceResult, CoScoreExercises1 coScoreExercises1) {
        String sUserName = null;
        try {
            sUserName = coScoreExercises1.getMaUser().getUserName();
            log.error("Eliminando la calificaci�n del estudiante: " + sUserName);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_SCORE_EXERCISE1);
            query.setParameter(1, coScoreExercises1.getCoExercises1().getExerciseId());
            query.setParameter(2, coScoreExercises1.getMaUser().getUserId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coScoreExercises1);
            Object[] arrayParam = { sUserName };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("coScoreExercises1.delete.success"), arrayParam));
            log.info("Eliminando la calificaci�n para el estudiante: " + sUserName);
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la calificaci�n: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coScoreExercises1.getMaUser().getUserName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("coScoreExercises1.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
