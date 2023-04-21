    public RestServiceResult update(RestServiceResult serviceResult, List listWordsComleteE3, Long nCompleteId) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_COMPLETE3_WORD);
            query.setParameter(1, new Long(nCompleteId));
            query.executeUpdate();
            EntityManagerHelper.commit();
            if (listWordsComleteE3 != null) {
                for (int i = 0; i < listWordsComleteE3.size(); i++) {
                    CoWordsCompleteE3 coWordsCompleteE3 = (CoWordsCompleteE3) listWordsComleteE3.get(i);
                    serviceResult = this.create(serviceResult, coWordsCompleteE3);
                }
            }
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar las palabras: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("multipleChoice.create.error"), e.getMessage()));
        }
        return serviceResult;
    }
