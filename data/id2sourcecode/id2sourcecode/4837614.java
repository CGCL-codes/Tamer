    @Override
    public boolean deleteTextId(Long textId) {
        if (textId != null) {
            Session session = HibernateUtil.getDefaultSessionFactory().getCurrentSession();
            boolean commit = false;
            Transaction t = session.getTransaction();
            if (!t.isActive()) {
                commit = true;
                t.begin();
            }
            SQLQuery q = session.createSQLQuery("delete from " + translationTable + " where text_id = " + textId);
            q.setFlushMode(FlushMode.MANUAL);
            int result;
            try {
                result = q.executeUpdate();
                if (commit) t.commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                if (commit) t.rollback();
                return false;
            }
            if (result > 0) {
                drop(textId);
                return true;
            }
        }
        return false;
    }
