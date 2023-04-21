    public RestServiceResult delete(RestServiceResult serviceResult, CoSingleTextCheckList3 coSingleTextCheckList) {
        String sTitle = null;
        try {
            sTitle = coSingleTextCheckList.getTitle();
            log.error("Eliminando la lista de chequeo: " + coSingleTextCheckList.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.getEntityManager().createNativeQuery(Statements.DELETE_CHECK_LIST_STUDENT);
            query.setParameter(1, coSingleTextCheckList.getCheckListId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sTitle };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("checkListStudent.delete.success"), arrayParam));
            log.info("Eliminando el curso: " + coSingleTextCheckList.getTitle());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el curso: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coSingleTextCheckList.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("checkListStudent.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
