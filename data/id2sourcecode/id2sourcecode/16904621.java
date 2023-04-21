    public RestServiceResult createQuestionMaterial(RestServiceResult serviceResult, String sArrayMaterialId, CoQuestion coQuestion) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_QUESTION_MATERIAL);
            query.setParameter(1, coQuestion.getQuestionId());
            query.executeUpdate();
            StringTokenizer stringTokenizer = new StringTokenizer(sArrayMaterialId, ",");
            while (stringTokenizer.hasMoreTokens()) {
                long nMaterialId = Long.parseLong(stringTokenizer.nextToken());
                query = EntityManagerHelper.createNativeQuery(Statements.INSERT_CO_QUESTION_MATERIAL);
                query.setParameter(1, coQuestion.getQuestionId());
                query.setParameter(2, nMaterialId);
                query.executeUpdate();
            }
            EntityManagerHelper.commit();
            Object[] arrayParam = { coQuestion.getQuestionName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("question.create.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Pregunta- Material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("question.create.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
