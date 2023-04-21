    public void cloneCourse(Long nCourseOldId, Long nCourseNewId, MaUser maUser) {
        try {
            Query query;
            CoCourse coCourse = new CoCourseDAO().findById(nCourseOldId);
            EntityManagerHelper.refresh(coCourse);
            EntityManagerHelper.beginTransaction();
            cloneUserHistory(nCourseOldId, nCourseNewId);
            EntityManagerHelper.commit();
            if (coCourse.getCoActivities().size() > 0) {
                Set<CoActivity> setActivity = coCourse.getCoActivities();
                for (CoActivity coActivity : setActivity) {
                    Long nActivityNewId = getSequence("sq_co_activity");
                    log.info("Clonando Curso Solo actividades. Numero: " + coCourse.getCourseId());
                    EntityManagerHelper.beginTransaction();
                    query = EntityManagerHelper.createNativeQuery(Statements.CLONE_ACTIVITY_FOR_COURSE.replaceAll(":CLONE", bundle.getString("course.create.clone")));
                    query.setParameter(1, nActivityNewId);
                    query.setParameter(2, nCourseNewId);
                    query.setParameter(3, coActivity.getActivityId());
                    query.executeUpdate();
                    EntityManagerHelper.commit();
                    log.info("OK...");
                    CoActivity coActivityNew = new CoActivityDAO().findById(nActivityNewId);
                    EntityManagerHelper.refresh(coActivityNew);
                    if (coActivityNew != null) {
                        EntityManagerHelper.beginTransaction();
                        DataManagerActivity.addUserHistory(new RestServiceResult(), maUser, coActivityNew);
                        query = EntityManagerHelper.createNativeQuery(Statements.CLONE_ACTIVITY_MATERIAL);
                        query.setParameter(1, coActivityNew.getActivityId());
                        query.setParameter(2, coActivityNew.getActivityParentId());
                        query.executeUpdate();
                        EntityManagerHelper.commit();
                        new DataManagerActivity().cloneActivity(coActivityNew.getActivityParentId(), coActivityNew.getActivityId());
                    }
                }
            } else {
                Set<CoUnit> setUnit = coCourse.getCoUnits();
                for (CoUnit coUnit : setUnit) {
                    EntityManagerHelper.beginTransaction();
                    Long nUnitNewId = getSequence("sq_co_unit");
                    query = EntityManagerHelper.createNativeQuery(Statements.CLONE_UNIT.replaceAll(":CLONE", bundle.getString("course.create.clone")));
                    query.setParameter(1, nUnitNewId);
                    query.setParameter(2, nCourseNewId);
                    query.setParameter(3, coUnit.getUnitId());
                    query.executeUpdate();
                    EntityManagerHelper.commit();
                    CoUnit coUnitNew = new CoUnitDAO().findById(nUnitNewId);
                    EntityManagerHelper.refresh(coUnitNew);
                    if (coUnitNew != null) {
                        EntityManagerHelper.beginTransaction();
                        DataManagerUnit.addUserHistory(new RestServiceResult(), maUser, coUnitNew);
                        query = EntityManagerHelper.createNativeQuery(Statements.CLONE_UNIT_MATERIAL);
                        query.setParameter(1, nUnitNewId);
                        query.setParameter(2, coUnit.getUnitId());
                        query.executeUpdate();
                        EntityManagerHelper.commit();
                        DataManagerUnit dataManagerUnit = new DataManagerUnit();
                        dataManagerUnit.setBundle(bundle);
                        dataManagerUnit.cloneUnit(coUnit.getUnitId(), nUnitNewId, maUser);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            EntityManagerHelper.rollback();
        }
    }
