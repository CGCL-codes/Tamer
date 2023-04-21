    public RestServiceResult createMenuMaterial(RestServiceResult serviceResult, CoMaterial coMaterial, CoMenu coMenu) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.INSERT_CO_MENU_MATERIAL);
            query.setParameter(1, coMenu.getMenuId());
            query.setParameter(2, coMaterial.getMaterialId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coMaterial.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("menu.createMenuUser.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Menu - Material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("menu.createMenuUser.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
