    public void synchronizeUserGroup(Long nExerciseGroupId, String sArrayUserId) {
        List<MaUser> listUserGroup = null;
        int nNumUserGroup = 0;
        int nNumArrayUser = 0;
        String sSql = null;
        try {
            sSql = Statements.UPDATE_FLAG_Y_USER_EXER1_GROUP;
            sSql = sSql.replaceFirst("v1", sArrayUserId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            query.setParameter(1, "Y");
            query.setParameter(2, nExerciseGroupId);
            int nDeleted = query.executeUpdate();
            sSql = Statements.UPDATE_FLAG_N_USER_EXER1_GROUP;
            sSql = sSql.replaceFirst("v1", sArrayUserId);
            query = EntityManagerHelper.createNativeQuery(sSql);
            query.setParameter(1, "N");
            query.setParameter(2, nExerciseGroupId);
            nDeleted = query.executeUpdate();
            sSql = Statements.SELECT_MA_USER_IN;
            sSql = sSql.replaceFirst("v1", sArrayUserId);
            query = EntityManagerHelper.createNativeQuery(sSql, MaUser.class);
            query.setParameter(1, nExerciseGroupId);
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            listUserGroup = query.getResultList();
            nNumArrayUser = listUserGroup.size();
            for (Iterator iterator = listUserGroup.iterator(); iterator.hasNext(); ) {
                MaUser maUser = (MaUser) iterator.next();
                query = EntityManagerHelper.createNativeQuery(Statements.SELECT_CO_USER_EXER1_GROUP_USER, CoUserExer1Group.class);
                query.setParameter(1, maUser.getUserId());
                query.setParameter(2, nExerciseGroupId);
                query.setHint(QueryHints.REFRESH, HintValues.TRUE);
                Vector vecResult = (Vector) query.getResultList();
                if (vecResult.size() == 0) {
                    CoUserExer1GroupId coUserExer1GroupId = new CoUserExer1GroupId(nExerciseGroupId, maUser.getUserId());
                    CoUserExer1Group coUserExer1Group = new CoUserExer1Group();
                    coUserExer1Group.setToExercise1Group(new ToExercise1GroupDAO().findById(nExerciseGroupId));
                    coUserExer1Group.setMaUser(maUser);
                    coUserExer1Group.setId(coUserExer1GroupId);
                    coUserExer1Group.setFlagDeleted("N");
                    new CoUserExer1GroupDAO().save(coUserExer1Group);
                } else {
                }
            }
            EntityManagerHelper.commit();
        } catch (Exception e) {
            log.info("Error buscando el estado para usuarios por grupo ");
            EntityManagerHelper.rollback();
        }
    }
