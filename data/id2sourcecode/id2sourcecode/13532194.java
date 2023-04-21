    public void deleteByCondition(String condition) {
        Transaction tx = getSession().beginTransaction();
        try {
            String hql = "delete RoleUser ";
            if (null != condition) {
                hql += "where ";
                hql += condition;
            }
            getSession().createQuery(hql).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
    }
