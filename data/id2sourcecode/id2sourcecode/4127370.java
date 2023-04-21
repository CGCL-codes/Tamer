    public RestServiceResult delete(RestServiceResult serviceResult, CoMultipleChoiceE1 coMultipleChoiceE1) {
        String sMultipleChoiceE1Name = null;
        try {
            sMultipleChoiceE1Name = coMultipleChoiceE1.getMultipleChoiceName();
            log.error("Eliminando la pregunta de seleccion: " + coMultipleChoiceE1.getMultipleChoiceName());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_MULTIPLE_CHOICE_E1);
            query.setParameter(1, coMultipleChoiceE1.getMultipleChoiceE1Id());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coMultipleChoiceE1);
            Object[] arrayParam = { sMultipleChoiceE1Name };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("multipleChoice.delete.success"), arrayParam));
            log.info("Eliminando la pregunta de seleccion: " + coMultipleChoiceE1.getMultipleChoiceName());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la pregunta de seleccion: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coMultipleChoiceE1.getMultipleChoiceName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("multipleChoice.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
