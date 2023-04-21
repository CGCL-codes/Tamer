    public RestServiceResult delete(RestServiceResult serviceResult, CoMenu coMenu) {
        try {
            log.info("Eliminando la menu: " + coMenu.getMenuName());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_MENU);
            query.setParameter(1, coMenu.getMenuId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coMenu.getMenuName() };
            log.info("Menu eliminada con �xito: " + coMenu.getMenuName());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("menu.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la opci�n de men�: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coMenu.getMenuName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("menu.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
