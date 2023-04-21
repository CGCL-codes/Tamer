    @SuppressWarnings("unchecked")
    public RestServiceResult addStudent(RestServiceResult serviceResult, Long nCourseId, String sArrayStudent, boolean isFile) {
        List<MaUser> listUserInsert = null;
        String sSql = "";
        Query query = null;
        try {
            CoCourse coCourse = new CoCourseDAO().findById(nCourseId);
            if (coCourse == null) {
                serviceResult.setMessage(bundle.getString("course.search.notFound"));
                serviceResult.setError(true);
            } else {
                EntityManagerHelper.beginTransaction();
                if (!isFile) {
                    sSql = Statements.UPDATE_FLAG_Y_STUDENTS;
                    sSql = sSql.replaceFirst("v1", sArrayStudent);
                    query = EntityManagerHelper.createNativeQuery(sSql);
                    query.setParameter(1, "Y");
                    query.setParameter(2, nCourseId);
                    query.executeUpdate();
                }
                sSql = Statements.UPDATE_FLAG_N_STUDENTS;
                sSql = sSql.replaceFirst("v1", sArrayStudent);
                query = EntityManagerHelper.createNativeQuery(sSql);
                query.setParameter(1, "N");
                query.setParameter(2, nCourseId);
                query.executeUpdate();
                sSql = Statements.SELECT_MA_USER_IN;
                sSql = sSql.replaceFirst("v1", sArrayStudent);
                query = EntityManagerHelper.createNativeQuery(sSql, MaUser.class);
                query.setHint(QueryHints.REFRESH, HintValues.TRUE);
                listUserInsert = query.getResultList();
                for (Iterator<MaUser> iterator = listUserInsert.iterator(); iterator.hasNext(); ) {
                    MaUser maUser = iterator.next();
                    query = EntityManagerHelper.createNativeQuery(Statements.SELECT_EXIST_STUDENT_COURSE, CoCourseUser.class);
                    query.setParameter(1, maUser.getUserId());
                    query.setParameter(2, nCourseId);
                    query.setHint(QueryHints.REFRESH, HintValues.TRUE);
                    Vector vecResult = (Vector) query.getResultList();
                    if (vecResult.size() == 0) {
                        CoCourseUserId coCourseUserHistoryId = new CoCourseUserId(maUser.getUserId(), coCourse.getCourseId());
                        CoCourseUser coCourseUser = new CoCourseUser();
                        coCourseUser.setCoCourse(coCourse);
                        coCourseUser.setMaUser(maUser);
                        coCourseUser.setFlagDeleted("N");
                        coCourseUser.setId(coCourseUserHistoryId);
                        new CoCourseUserDAO().save(coCourseUser);
                    } else {
                    }
                }
                EntityManagerHelper.commit();
                Object[] arrayParam = { coCourse.getCourseName() };
                serviceResult.setMessage(MessageFormat.format(bundle.getString("course.addStudent.success"), arrayParam));
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            Object[] arrayParam = { e.getMessage() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("course.addStudent.error)"), arrayParam));
            serviceResult.setError(true);
            EntityManagerHelper.rollback();
        }
        return serviceResult;
    }
