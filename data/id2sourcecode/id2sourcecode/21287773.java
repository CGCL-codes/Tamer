    public void synchronizeUserGroup(Long nExerciseGroupId, String sArrayUserId) {
        List<MaUser> listUserGroup = null;
        int nNumUserGroup = 0;
        int nNumArrayUser = 0;
        String sSql = null;
        try {
            sSql = Statements.UPDATE_FLAG_Y_USER_EXER2_GROUP;
            sSql = sSql.replaceFirst("v1", sArrayUserId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            query.setParameter(1, "Y");
            query.setParameter(2, nExerciseGroupId);
            int nDeleted = query.executeUpdate();
            sSql = Statements.UPDATE_FLAG_N_USER_EXER2_GROUP;
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
                query = EntityManagerHelper.createNativeQuery(Statements.SELECT_CO_USER_EXER2_GROUP_USER, CoUserExer2Group.class);
                query.setParameter(1, maUser.getUserId());
                query.setParameter(2, nExerciseGroupId);
                query.setHint(QueryHints.REFRESH, HintValues.TRUE);
                Vector vecResult = (Vector) query.getResultList();
                if (vecResult.size() == 0) {
                    CoUserExer2GroupId CoUserExer2GroupId = new CoUserExer2GroupId(nExerciseGroupId, maUser.getUserId());
                    CoUserExer2Group CoUserExer2Group = new CoUserExer2Group();
                    CoUserExer2Group.setToExercise2Group(new ToExercise2GroupDAO().findById(nExerciseGroupId));
                    CoUserExer2Group.setMaUser(maUser);
                    CoUserExer2Group.setId(CoUserExer2GroupId);
                    CoUserExer2Group.setFlagDeleted("N");
                    new CoUserExer2GroupDAO().save(CoUserExer2Group);
                } else {
                }
            }
            EntityManagerHelper.commit();
        } catch (Exception e) {
            log.info("Error buscando el estado para usuarios por grupo ");
            EntityManagerHelper.rollback();
        }
    }
