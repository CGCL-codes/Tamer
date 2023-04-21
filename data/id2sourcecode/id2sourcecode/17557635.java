    public RestServiceResult delete(RestServiceResult serviceResult, ToAssistance maAssistance) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.getEntityManager().createNativeQuery(Statements.DELETE_TO_ASSISTANCE);
            query.setParameter(1, maAssistance.getAssistanceId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(maAssistance);
            Object[] arrayParam = { maAssistance.getAssistanceId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("assistance.delete.success"), arrayParam));
            log.info("Eliminando la configuraci�n del asistente: " + maAssistance.getAssistanceId());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la configuraci�n del asistente: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { maAssistance.getAssistanceId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("assistance.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
