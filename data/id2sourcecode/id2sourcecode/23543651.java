    public void deleteAcDataProject(Integer projectId, String[] userIds) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String delStr = null;
            for (int i = 0; i < userIds.length; i++) {
                if (userIds[i] == null) {
                    continue;
                }
                delStr = "delete from " + AcDataProject.class.getName() + " where data.id = '" + projectId + "' and viewer.id = '" + userIds[i] + "'";
                Query queryObject = getSession().createQuery(delStr);
                queryObject.executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }
