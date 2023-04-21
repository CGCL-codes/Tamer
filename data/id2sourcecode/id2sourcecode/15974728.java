    public RestServiceResult delete(RestServiceResult serviceResult, ToGloss maGloss) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.getEntityManager().createNativeQuery(Statements.DELETE_TO_GLOSS);
            query.setParameter(1, maGloss.getGlossId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(maGloss);
            Object[] arrayParam = { maGloss.getGlossId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("gloss.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el texto individual: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { maGloss.getGlossId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("gloss.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
