    public RestServiceResult delete(RestServiceResult serviceResult, ToExercise1Group toExercise1Group) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_EXERCITE1_GROUP);
            query.setParameter(1, toExercise1Group.getExerciseGroupId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toExercise1Group);
            Object[] arrayParam = { toExercise1Group.getGroupName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercise1group.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el ejercicio grupal s1: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toExercise1Group.getGroupName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercise1group.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
