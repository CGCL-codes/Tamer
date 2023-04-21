    public RestServiceResult delete(RestServiceResult serviceResult, CoScoreExercises2 coScoreExercises2) {
        String sUserName = null;
        try {
            sUserName = coScoreExercises2.getMaUser().getUserName();
            log.error("Eliminando la calificaci�n del estudiante: " + sUserName);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_SCORE_EXERCISE2);
            query.setParameter(1, coScoreExercises2.getCoExercises2().getExerciseId());
            query.setParameter(2, coScoreExercises2.getMaUser().getUserId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coScoreExercises2);
            Object[] arrayParam = { sUserName };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("coScoreExercises2.delete.success"), arrayParam));
            log.info("Eliminando la calificaci�n para el estudiante: " + sUserName);
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la calificaci�n: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coScoreExercises2.getMaUser().getUserName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("coScoreExercises2.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
