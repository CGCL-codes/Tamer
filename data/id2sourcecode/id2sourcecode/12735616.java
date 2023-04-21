    public RestServiceResult delete(RestServiceResult serviceResult, CoScoreQuestion coScoreQuestion) {
        String sUserName = null;
        try {
            sUserName = coScoreQuestion.getMaUser().getUserName();
            log.error("Eliminando la calificaci�n del estudiante: " + sUserName);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_SCORE_QUESTION);
            query.setParameter(1, coScoreQuestion.getCoQuestion().getQuestionId());
            query.setParameter(2, coScoreQuestion.getMaUser().getUserId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coScoreQuestion);
            Object[] arrayParam = { sUserName };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("coScoreQuestion.delete.success"), arrayParam));
            log.info("Eliminando la calificaci�n para el estudiante: " + sUserName);
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la calificaci�n: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coScoreQuestion.getMaUser().getUserName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("coScoreQuestion.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
