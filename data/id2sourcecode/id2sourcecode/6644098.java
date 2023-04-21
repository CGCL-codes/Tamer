    public RestServiceResult delete(RestServiceResult serviceResult, MaSpell maSpell) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_MA_SPELL);
            query.setParameter(1, maSpell.getWordId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { maSpell.getWord() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("spell.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la palbra: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { maSpell.getWord() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("spell.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
