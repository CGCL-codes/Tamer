    public Integer updateWordsList(List<Words> wordsList, List<Words> updateReplaceList) {
        int updateNumber = 0;
        if (wordsList == null && wordsList.size() <= 0) return updateNumber;
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            for (int i = 0; i < wordsList.size(); i++) {
                Words words = wordsList.get(i);
                if (words != null && words.getFind() != null) {
                    Query query = session.createQuery("update Words as w set w.find = :find  where w.id=:id");
                    query.setString("find", words.getFind());
                    query.setShort("id", words.getId());
                    updateNumber += query.executeUpdate();
                }
            }
            for (int i = 0; i < updateReplaceList.size(); i++) {
                Words words = updateReplaceList.get(i);
                Query query = session.createQuery("update Words as w set w.replacement=:replacement where w.id=:id");
                query.setString("replacement", words.getReplacement());
                query.setShort("id", words.getId());
                updateNumber += query.executeUpdate();
            }
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
        }
        return null;
    }
