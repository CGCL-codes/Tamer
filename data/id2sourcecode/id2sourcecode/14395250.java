    public RestServiceResult createExerciseS2Material(RestServiceResult serviceResult, String sArrayMaterialId, CoExercises2 coExercises2) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_EXERCITES2_MATERIAL);
            query.setParameter(1, coExercises2.getExerciseId());
            query.executeUpdate();
            StringTokenizer stringTokenizer = new StringTokenizer(sArrayMaterialId, ",");
            while (stringTokenizer.hasMoreTokens()) {
                long nMaterialId = Long.parseLong(stringTokenizer.nextToken());
                query = EntityManagerHelper.createNativeQuery(Statements.INSERT_CO_EXERCITES2_MATERIAL);
                query.setParameter(1, coExercises2.getExerciseId());
                query.setParameter(2, nMaterialId);
                query.executeUpdate();
            }
            EntityManagerHelper.commit();
            Object[] arrayParam = { coExercises2.getExerciseName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercises2.create.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Ejercicio2- Material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercises2.create.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
