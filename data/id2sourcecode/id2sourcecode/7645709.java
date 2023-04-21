    public RestServiceResult delete(RestServiceResult serviceResult, CoSequence coSequence) {
        try {
            log.info("Eliminando la secuencia: " + coSequence.getSequenceName());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_SEQUENCE);
            query.setParameter(1, coSequence.getSequenceId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coSequence);
            Object[] arrayParam = { coSequence.getSequenceName() };
            log.info("Secuencia eliminada con �xito: " + coSequence.getSequenceName());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("sequence.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la secuencia: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coSequence.getSequenceName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("sequence.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
