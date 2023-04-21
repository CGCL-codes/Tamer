    public void cloneActivity(Long nActivityOldId, Long nActivityNewId) {
        try {
            CoActivity coActivityOld = new CoActivityDAO().findById(new Long(nActivityOldId));
            EntityManagerHelper.refresh(nActivityOldId);
            Set<CoExercises1> setExer1 = coActivityOld.getCoExercises1s();
            log.info(" CLONADO ACTIVIDAD: " + nActivityOldId + " ACTIVIDAD NUEVA: " + nActivityNewId);
            if (setExer1.size() != 0) {
                for (Iterator<CoExercises1> iterator = setExer1.iterator(); iterator.hasNext(); ) {
                    CoExercises1 coExercises1Old = iterator.next();
                    EntityManagerHelper.refresh(coExercises1Old);
                    Long nExercises1NewId = getSequence("sq_co_exercises1");
                    Long nExercises1OldId = coExercises1Old.getExerciseId();
                    EntityManagerHelper.beginTransaction();
                    Query query = EntityManagerHelper.createNativeQuery(Statements.CLONE_EXERCISES_1);
                    query.setParameter(1, nExercises1NewId);
                    query.setParameter(2, nActivityNewId);
                    query.setParameter(3, nExercises1OldId);
                    int nUpdate = query.executeUpdate();
                    log.info("Clonaci�n EJERCICIO_T1[" + nExercises1NewId + "] = Modificados: " + nUpdate);
                    EntityManagerHelper.commit();
                    CoExercises1 coExercises1 = new CoExercises1DAO().findById(nExercises1NewId);
                    EntityManagerHelper.refresh(coExercises1);
                    if (coExercises1 != null) {
                        EntityManagerHelper.beginTransaction();
                        query = EntityManagerHelper.createNativeQuery(Statements.CLONE_EXERCISES1_MATERIAL);
                        query.setParameter(1, nExercises1NewId);
                        query.setParameter(2, nExercises1OldId);
                        nUpdate = query.executeUpdate();
                        log.info("Clonaci�n[" + nExercises1OldId + "]- MATERIALES_EJERCICIO_T1[" + nExercises1NewId + "]" + " - Materiales =>  " + nUpdate);
                        EntityManagerHelper.commit();
                        DataManagerExerciseS1 dataManagerExerciseS1 = new DataManagerExerciseS1();
                        dataManagerExerciseS1.setBundle(bundle);
                        dataManagerExerciseS1.cloneExercises1(nExercises1OldId, nExercises1NewId);
                    }
                }
            } else {
                Set<CoExercises2> setExer2 = coActivityOld.getCoExercises2s();
                if (setExer2.size() != 0) {
                    for (Iterator<CoExercises2> iterator = setExer2.iterator(); iterator.hasNext(); ) {
                        EntityManagerHelper.beginTransaction();
                        CoExercises2 coExercises2Old = iterator.next();
                        Long nExercises2NewId = getSequence("sq_co_exercises2");
                        Long nExercises2OldId = coExercises2Old.getExerciseId();
                        Query query = EntityManagerHelper.createNativeQuery(Statements.CLONE_EXERCISES_2);
                        query.setParameter(1, nExercises2NewId);
                        query.setParameter(2, nActivityNewId);
                        query.setParameter(3, nExercises2OldId);
                        int nUpdate = query.executeUpdate();
                        log.info("Clonaci�n EJERCICIO_T2[" + nExercises2NewId + "] = Modificados:  => " + nUpdate);
                        EntityManagerHelper.commit();
                        CoExercises2 coExercises2 = new CoExercises2DAO().findById(nExercises2NewId);
                        EntityManagerHelper.refresh(coExercises2);
                        if (coExercises2 != null) {
                            EntityManagerHelper.beginTransaction();
                            query = EntityManagerHelper.createNativeQuery(Statements.CLONE_EXERCISES2_MATERIAL);
                            query.setParameter(1, nExercises2NewId);
                            query.setParameter(2, nExercises2OldId);
                            nUpdate = query.executeUpdate();
                            log.info("Clonaci�n[" + nExercises2OldId + "] - MATERIALES_EJERCICIO_T2[" + nExercises2NewId + "]" + " - Materiales  => " + nUpdate);
                            EntityManagerHelper.commit();
                            DataManagerExerciseS2 dataManagerExerciseS2 = new DataManagerExerciseS2();
                            dataManagerExerciseS2.setBundle(bundle);
                            dataManagerExerciseS2.cloneExercises2(nExercises2OldId, nExercises2NewId);
                        }
                    }
                }
            }
            EntityManagerHelper.beginTransaction();
            cloneUserHistory(nActivityOldId, nActivityNewId);
            log.info("Clonaci�n Historial de actividades OK...");
            EntityManagerHelper.commit();
        } catch (Exception e) {
            e.printStackTrace();
            EntityManagerHelper.rollback();
        }
    }
