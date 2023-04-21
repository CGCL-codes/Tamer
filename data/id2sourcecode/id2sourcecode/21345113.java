    public RestServiceResult delete(RestServiceResult serviceResult, MaPostag maPostag) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_MA_POSTAG);
            query.setParameter(1, maPostag.getPostagId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { maPostag.getTag() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("postag.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el postag: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { maPostag.getTag() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("postag.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
