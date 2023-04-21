    public RestServiceResult delete(RestServiceResult serviceResult, MaSpellError maSpellError) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_MA_SPELL_ERROR);
            query.setParameter(1, maSpellError.getErrorId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { maSpellError.getWrongWord() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("spellerror.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el error: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { maSpellError.getWrongWord() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("spellerror.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
