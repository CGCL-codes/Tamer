    public RestServiceResult delete(RestServiceResult serviceResult, ToLinks toLinks) {
        try {
            log.info("Eliminando la publicacion: " + toLinks.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_LINKS);
            query.setParameter(1, toLinks.getLinksId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toLinks);
            Object[] arrayParam = { toLinks.getTitle() };
            log.info("Publicacion eliminada con �xito: " + toLinks.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("links.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la publicacion: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toLinks.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("links.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
