    @SuppressWarnings("unchecked")
    public RestServiceResult removeStudent(RestServiceResult serviceResult, Long nCourseId, String sArrayStudent) {
        String sSql = "";
        Query query = null;
        try {
            CoCourse coCourse = new CoCourseDAO().findById(nCourseId);
            if (coCourse == null) {
                serviceResult.setMessage(bundle.getString("course.search.notFound"));
                serviceResult.setError(true);
            } else {
                EntityManagerHelper.beginTransaction();
                sSql = Statements.REMOVE_STUDENTS_IN_COURSE;
                sSql = sSql.replaceFirst("v1", sArrayStudent);
                query = EntityManagerHelper.createNativeQuery(sSql);
                query.setParameter(1, nCourseId);
                query.executeUpdate();
                EntityManagerHelper.commit();
                Object[] arrayParam = { coCourse.getCourseName() };
                serviceResult.setMessage(MessageFormat.format(bundle.getString("course.removeStudent.success"), arrayParam));
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            Object[] arrayParam = { e.getMessage() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("course.removeStudent.error)"), arrayParam));
            serviceResult.setError(true);
            EntityManagerHelper.rollback();
        }
        return serviceResult;
    }
