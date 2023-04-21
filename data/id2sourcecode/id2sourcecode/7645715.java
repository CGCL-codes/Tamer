    public void cloneSequence(Long nSequenceOldId, Long nSequenceNewId, MaUser maUser) {
        try {
            CoSequence coSequence = new CoSequenceDAO().findById(nSequenceOldId);
            EntityManagerHelper.refresh(coSequence);
            EntityManagerHelper.beginTransaction();
            cloneUserHistory(nSequenceOldId, nSequenceNewId);
            EntityManagerHelper.commit();
            Set<CoActivity> setActivity = coSequence.getCoActivities();
            log.info("N�mero de actividades a clonar: " + setActivity.size());
            for (CoActivity coActivity : setActivity) {
                EntityManagerHelper.beginTransaction();
                log.info("Clonado actividad: " + coActivity.getActivityId());
                Long nActivityNewId = getSequence("sq_co_activity");
                Query query = EntityManagerHelper.createNativeQuery(Statements.CLONE_ACTIVITY_FOR_SEQUENCE.replaceAll(":CLONE", bundle.getString("course.create.clone")));
                query.setParameter(1, nActivityNewId);
                query.setParameter(2, nSequenceNewId);
                query.setParameter(3, coActivity.getActivityId());
                query.executeUpdate();
                EntityManagerHelper.commit();
                CoActivity coActivityNew = new CoActivityDAO().findById(nActivityNewId);
                EntityManagerHelper.refresh(coActivityNew);
                if (coActivityNew != null) {
                    EntityManagerHelper.beginTransaction();
                    DataManagerActivity.addUserHistory(new RestServiceResult(), maUser, coActivityNew);
                    query = EntityManagerHelper.createNativeQuery(Statements.CLONE_ACTIVITY_MATERIAL);
                    query.setParameter(1, nActivityNewId);
                    query.setParameter(2, coActivity.getActivityId());
                    query.executeUpdate();
                    log.info("Ok...");
                    EntityManagerHelper.commit();
                    DataManagerActivity dataManagerActivity = new DataManagerActivity();
                    dataManagerActivity.setBundle(bundle);
                    dataManagerActivity.cloneActivity(coActivity.getActivityId(), nActivityNewId);
                }
            }
            Set<CoTest> setTestClone = coSequence.getCoTests();
            log.info("N�mero de pruebas a clonar:_" + setTestClone.size());
            for (CoTest coTest : setTestClone) {
                EntityManagerHelper.beginTransaction();
                Long nTestNewId = getSequence("sq_co_test");
                Query query = EntityManagerHelper.createNativeQuery(Statements.CLONE_TEST.replaceAll(":CLONE", bundle.getString("course.create.clone")));
                query.setParameter(1, nTestNewId);
                query.setParameter(2, nSequenceNewId);
                query.setParameter(3, coTest.getTestId());
                query.executeUpdate();
                EntityManagerHelper.commit();
                log.info("Clonando prueba: " + coTest.getTestId());
                DataManagerTest.addUserHistory(new RestServiceResult(), maUser, coTest);
                CoTest coTestNew = new CoTestDAO().findById(new Long(nTestNewId));
                EntityManagerHelper.refresh(coTestNew);
                if (coTestNew != null) {
                    EntityManagerHelper.beginTransaction();
                    query = EntityManagerHelper.createNativeQuery(Statements.CLONE_TEST_MATERIAL);
                    query.setParameter(1, nTestNewId);
                    query.setParameter(2, coTest.getTestId());
                    query.executeUpdate();
                    query = EntityManagerHelper.createNativeQuery(Statements.CLONE_QUESTION_WEIGHTED);
                    query.setParameter(1, nTestNewId);
                    query.setParameter(2, coTest.getTestId());
                    query.executeUpdate();
                    log.info("Ok...");
                    EntityManagerHelper.commit();
                    DataManagerTest dataManagerTest = new DataManagerTest();
                    dataManagerTest.setBundle(bundle);
                    dataManagerTest.cloneTest(coTest.getTestId(), nTestNewId);
                }
            }
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            e.printStackTrace();
        }
    }
