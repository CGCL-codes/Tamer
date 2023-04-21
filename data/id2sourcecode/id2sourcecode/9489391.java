    public RestServiceResult delete(RestServiceResult serviceResult, CoMultipleChoiceE3 coMultipleChoiceE3) {
        String sMultipleChoiceE3Name = null;
        try {
            sMultipleChoiceE3Name = coMultipleChoiceE3.getMultipleChoiceName();
            log.error("Eliminando la pregunta de seleccion: " + coMultipleChoiceE3.getMultipleChoiceName());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_MULTIPLE_CHOICE_E3);
            query.setParameter(1, coMultipleChoiceE3.getMultipleChoiceE3Id());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coMultipleChoiceE3);
            Object[] arrayParam = { sMultipleChoiceE3Name };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("multipleChoice.delete.success"), arrayParam));
            log.info("Eliminando la pregunta de seleccion: " + coMultipleChoiceE3.getMultipleChoiceName());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el curso: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coMultipleChoiceE3.getMultipleChoiceName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("multipleChoice.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
