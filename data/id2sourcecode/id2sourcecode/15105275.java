    public RestServiceResult delete(RestServiceResult serviceResult, MaParagraphCheckList maSingleParagraphCheckList) {
        String sTitle = null;
        try {
            sTitle = maSingleParagraphCheckList.getTitle();
            log.error("Eliminando la lista de chequeo: " + maSingleParagraphCheckList.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.getEntityManager().createNativeQuery(Statements.DELETE_PARAGRAPH_CHECK_LIST);
            query.setParameter(1, maSingleParagraphCheckList.getCheckListFormId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sTitle };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("checkList.delete.success"), arrayParam));
            log.info("Eliminando el curso: " + maSingleParagraphCheckList.getTitle());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el curso: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { maSingleParagraphCheckList.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("checkList.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
