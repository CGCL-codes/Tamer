    public void deleteRoleAction(Integer roleId, String[] actionIds) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String delStr = null;
            for (int i = 0; i < actionIds.length; i++) {
                delStr = "delete from " + AcRoleAction.class.getName() + " where acRole.id = '" + roleId + "' and acAction.id = '" + actionIds[i] + "'";
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
