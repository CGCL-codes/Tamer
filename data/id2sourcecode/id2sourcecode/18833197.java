    public RestServiceResult delete(RestServiceResult serviceResult, CoCourse coCourse) {
        String sCourseName = null;
        try {
            sCourseName = coCourse.getCourseName();
            log.error("Eliminando el curso: " + coCourse.getCourseName());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_COURSE);
            query.setParameter(1, coCourse.getCourseId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sCourseName };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("course.delete.success"), arrayParam));
            log.info("Eliminando el curso: " + coCourse.getCourseName());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el curso: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coCourse.getCourseName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("course.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
