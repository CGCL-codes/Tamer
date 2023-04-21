    public RestServiceResult createPublicationComment(RestServiceResult serviceResult, String sPublicationId, ToComment toComment) {
        try {
            Query query = EntityManagerHelper.createNativeQuery(Statements.INSERT_CO_PUBLICATION_COMMENT);
            query.setParameter(1, toComment.getCommentId());
            query.setParameter(2, new Long(sPublicationId));
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { toComment.getCommentId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("comment.create.success"), arrayParam));
        } catch (PersistenceException e) {
            e.printStackTrace();
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Publicacion - Material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("comment.create.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
