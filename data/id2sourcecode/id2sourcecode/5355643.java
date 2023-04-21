    public RestServiceResult delete(RestServiceResult serviceResult, ToGlossary toGlossary) {
        String sTerm = null;
        try {
            sTerm = toGlossary.getTerm();
            log.error("Eliminando el termino del glosario : " + toGlossary.getTerm());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_GLOSSARY);
            query.setParameter(1, toGlossary.getGlossaryId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sTerm };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("glossary.delete.success"), arrayParam));
            log.info("Eliminando el termino del glosario : " + toGlossary.getTerm());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el termino del glosario : " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { toGlossary.getTerm() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("glossary.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
