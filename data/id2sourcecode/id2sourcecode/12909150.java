    public void setIsViewed(Integer mboxId, Integer userId) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String updateString = "update " + SysMboxViewer.class.getName() + " set isViewed=true where sysMbox.id=? and acUser.id=?";
            Query queryObject = getSession().createQuery(updateString);
            queryObject.setParameter(0, mboxId);
            queryObject.setParameter(1, userId);
            queryObject.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }
