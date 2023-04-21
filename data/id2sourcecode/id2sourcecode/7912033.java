    public RestServiceResult delete(RestServiceResult serviceResult, CoParagraphBaseKnowledge coParagraphBaseKnowledge) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_PARAGRAPH_BASE_KNOWLEDGE);
            query.setParameter(1, coParagraphBaseKnowledge.getKnowledgeId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coParagraphBaseKnowledge.getKnowledgeId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("paragraphBaseKnowledge.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el Knowledge: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coParagraphBaseKnowledge.getKnowledgeId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("paragraphBaseKnowledge.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
