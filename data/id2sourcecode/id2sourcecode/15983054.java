    public RestServiceResult delete(RestServiceResult serviceResult, ToNotebook toNotebook) {
        try {
            log.info("Eliminando el diario: " + toNotebook.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_NOTEBOOK);
            query.setParameter(1, toNotebook.getNotebookId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toNotebook);
            Object[] arrayParam = { toNotebook.getTitle() };
            log.info("Diario eliminado con �xito: " + toNotebook.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("notebook.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el diario: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toNotebook.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("notebook.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
