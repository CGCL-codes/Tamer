    public RestServiceResult createUnitMaterial(RestServiceResult serviceResult, String sArrayMaterialId, CoUnit coUnit) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_UNIT_MATERIAL);
            query.setParameter(1, coUnit.getUnitId());
            query.executeUpdate();
            StringTokenizer stringTokenizer = new StringTokenizer(sArrayMaterialId, ",");
            while (stringTokenizer.hasMoreTokens()) {
                long nMaterialId = Long.parseLong(stringTokenizer.nextToken());
                query = EntityManagerHelper.createNativeQuery(Statements.INSERT_CO_UNIT_MATERIAL);
                query.setParameter(1, coUnit.getUnitId());
                query.setParameter(2, nMaterialId);
                query.executeUpdate();
            }
            EntityManagerHelper.commit();
            Object[] arrayParam = { coUnit.getUnitName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("unit.create.success"), arrayParam));
        } catch (PersistenceException e) {
            e.printStackTrace();
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Actividad - Material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("unit.create.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
