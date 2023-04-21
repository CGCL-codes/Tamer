    public RestServiceResult delete(RestServiceResult serviceResult, ToThemes toThemes) {
        try {
            log.info("Eliminando el tema: " + toThemes.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_FORUM);
            query.setParameter(1, toThemes.getThemeId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toThemes);
            Object[] arrayParam = { toThemes.getTitle() };
            log.info("Anuncio eliminado con �xito: " + toThemes.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("theme.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el tema: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toThemes.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("theme.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
