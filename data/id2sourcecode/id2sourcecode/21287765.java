    public RestServiceResult delete(RestServiceResult serviceResult, ToExercise2Group toExercise2Group) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_EXERCITE2_GROUP);
            query.setParameter(1, toExercise2Group.getExerciseGroupId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toExercise2Group);
            Object[] arrayParam = { toExercise2Group.getGroupName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercise2group.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el ejercicio grupal S2: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toExercise2Group.getGroupName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercise2group.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
