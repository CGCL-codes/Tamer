    public RestServiceResult delete(RestServiceResult serviceResult, CoTechnical coTechnical) {
        String sTechnicalName = null;
        CoTechnicalDAO coTechnicalDAO = new CoTechnicalDAO();
        try {
            sTechnicalName = coTechnical.getTechnicalName();
            log.error("Eliminando el Tecnica: " + coTechnical.getTechnicalName());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_TECHNICAL);
            query.setParameter(1, coTechnical.getTechnicalId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sTechnicalName };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("technical.delete.success"), arrayParam));
            log.info("Eliminando el curso: " + coTechnical.getTechnicalName());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el curso: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coTechnical.getTechnicalName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("technical.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
