    public RestServiceResult delete(RestServiceResult serviceResult, MaSyntaticError maSyntaticError) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_MA_SYNTACTIC_ERROR);
            query.setParameter(1, maSyntaticError.getErrorId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { maSyntaticError.getWrongSentence() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("syntacticerror.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el error sintactico: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { maSyntaticError.getWrongSentence() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("syntacticerror.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
