    public RestServiceResult createMenuUser(RestServiceResult serviceResult, CoMenu coMenu, Long nUserId) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.INSERT_CO_MENU_USER);
            query.setParameter(1, coMenu.getMenuId());
            query.setParameter(2, nUserId);
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coMenu.getMenuName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("menu.createMenuUser.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar la unit: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("menu.createMenuUser.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
