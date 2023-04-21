    public RestServiceResult delete(RestServiceResult serviceResult, MaSyntatic maSyntatic) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_MA_SYNTACTIC);
            query.setParameter(1, maSyntatic.getSytanticId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { maSyntatic.getSecuence() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("syntactic.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la unidad sintactica: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { maSyntatic.getSecuence() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("syntactic.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
