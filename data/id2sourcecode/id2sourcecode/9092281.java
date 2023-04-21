    public RestServiceResult createToLinksComment(RestServiceResult serviceResult, String sLinksId, ToComment toComment) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.INSERT_TO_LINKS_COMMENT);
            query.setParameter(1, toComment.getCommentId());
            query.setParameter(2, new Long(sLinksId));
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { toComment.getCommentId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("comment.create.success"), arrayParam));
        } catch (PersistenceException e) {
            e.printStackTrace();
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Enlace - Comentario: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("comment.create.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
