    public RestServiceResult delete(RestServiceResult serviceResult, MaSingleTextCheckList maSingleTextCheckList) {
        String sTitle = null;
        try {
            sTitle = maSingleTextCheckList.getTitle();
            log.error("Eliminando la lista de chequeo: " + maSingleTextCheckList.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.getEntityManager().createNativeQuery(Statements.DELETE_CHECK_LIST);
            query.setParameter(1, maSingleTextCheckList.getCheckListFormId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sTitle };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("checkList.delete.success"), arrayParam));
            log.info("Eliminando el curso: " + maSingleTextCheckList.getTitle());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el curso: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { maSingleTextCheckList.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("checkList.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
