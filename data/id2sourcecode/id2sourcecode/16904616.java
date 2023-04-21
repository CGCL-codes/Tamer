    public RestServiceResult delete(RestServiceResult serviceResult, CoQuestion coQuestion) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_QUESTION);
            query.setParameter(1, coQuestion.getQuestionId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coQuestion);
            Object[] arrayParam = { coQuestion.getQuestionName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("question.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar la pregunta: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coQuestion.getQuestionName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("question.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
