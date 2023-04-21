    public RestServiceResult createActivityMaterial(RestServiceResult serviceResult, String sArrayMaterialId, CoActivity coActivity) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_ACTIVITY_MATERIAL);
            query.setParameter(1, coActivity.getActivityId());
            query.executeUpdate();
            StringTokenizer stringTokenizer = new StringTokenizer(sArrayMaterialId, ",");
            while (stringTokenizer.hasMoreTokens()) {
                long nMaterialId = Long.parseLong(stringTokenizer.nextToken());
                query = EntityManagerHelper.createNativeQuery(Statements.INSERT_CO_ACTIVITY_MATERIAL);
                query.setParameter(1, coActivity.getActivityId());
                query.setParameter(2, nMaterialId);
                query.executeUpdate();
            }
            EntityManagerHelper.commit();
            Object[] arrayParam = { coActivity.getActivityName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("activity.create.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Actividad - Material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("activity.create.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
