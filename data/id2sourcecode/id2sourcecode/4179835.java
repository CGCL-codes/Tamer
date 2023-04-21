    public RestServiceResult delete(RestServiceResult serviceResult, ToNews toNews) {
        try {
            log.info("Eliminando el anuncio: " + toNews.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_NEWS);
            query.setParameter(1, toNews.getNewsId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toNews);
            Object[] arrayParam = { toNews.getTitle() };
            log.info("Anuncio eliminado con �xito: " + toNews.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("news.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el anuncio: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toNews.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("news.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
