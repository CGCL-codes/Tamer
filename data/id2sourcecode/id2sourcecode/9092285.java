    public RestServiceResult createToDictionaryComment(RestServiceResult serviceResult, String sDictionaryId, ToComment toComment) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.INSERT_TO_DICTIONARY_COMMENT);
            query.setParameter(1, toComment.getCommentId());
            query.setParameter(2, new Long(sDictionaryId));
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
