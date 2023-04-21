    public RestServiceResult delete(RestServiceResult serviceResult, MaSingleTextForm maSingleTextForm) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.getEntityManager().createNativeQuery(Statements.DELETE_MA_SINGLE_TEXT_FORM);
            query.setParameter(1, maSingleTextForm.getSingleTextFormId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(maSingleTextForm);
            Object[] arrayParam = { maSingleTextForm.getSingleTextFormId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("singleTextForm.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el texto individual: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { maSingleTextForm.getSingleTextFormId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("singleTextForm.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
