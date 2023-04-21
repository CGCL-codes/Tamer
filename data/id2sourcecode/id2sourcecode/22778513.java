    public RestServiceResult delete(RestServiceResult serviceResult, CoMatrixQuestion coMatrixQuestion) {
        String sQuestionName = null;
        try {
            sQuestionName = coMatrixQuestion.getCoQuestion().getQuestionName();
            log.error("Eliminando la matriz de preguntas: " + sQuestionName);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_MATRIX_QUESTION);
            query.setParameter(1, coMatrixQuestion.getMatrixId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sQuestionName };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("matrixQuestion.delete.success"), arrayParam));
            log.info("Eliminando matriz de preguntas: " + sQuestionName);
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar la matriz de preguntas: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coMatrixQuestion.getEnunciate() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("matrixQuestion.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
