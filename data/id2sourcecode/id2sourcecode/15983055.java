    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayNotebookId) {
        try {
            log.info("Eliminando DIARIO: " + sArrayNotebookId);
            String sSql = Statements.DELETE_MASIVE_NOTEBOOK;
            sSql = sSql.replaceFirst("v1", sArrayNotebookId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de DIARIO eliminados => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("notebook.delete.success"), arrayParam));
        } catch (Exception e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el diario: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("notebook.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
