    @SuppressWarnings("unchecked")
    @Override
    public Long resolveTextId(Long textId, String text, String languageId) {
        if (languageId == null) languageId = defaultLanguageId;
        if (text != null) text = "'" + text + "'"; else text = "null";
        Session session = HibernateUtil.getDefaultSessionFactory().getCurrentSession();
        boolean commit = false;
        Transaction t = session.getTransaction();
        if (!t.isActive()) {
            commit = true;
            t.begin();
        }
        try {
            if (textId != null) {
                SQLQuery q = session.createSQLQuery("UPDATE " + translationTable + " SET \"text\"=" + text + " WHERE text_id = " + textId + " AND language_id='" + languageId + "';");
                q.setFlushMode(FlushMode.MANUAL);
                int i = q.executeUpdate();
                if (i == 0) {
                    q = session.createSQLQuery("INSERT INTO translation(text_id, language_id, \"text\")VALUES (" + textId + ", '" + languageId + "', " + text + ")");
                    q.setFlushMode(FlushMode.MANUAL);
                    if (q.executeUpdate() > 0) store(textId, languageId, text);
                } else {
                    update(textId, languageId, text);
                }
                if (commit) t.commit();
                return textId;
            } else {
                SQLQuery q = session.createSQLQuery("SELECT nextval('" + translationTableSeq + "') as r");
                q.setFlushMode(FlushMode.MANUAL);
                q.addScalar("r", Hibernate.LONG);
                List<Object> l = q.list();
                textId = (Long) l.get(0);
                q = session.createSQLQuery("INSERT INTO translation(text_id, language_id, \"text\")VALUES (" + textId + ", '" + languageId + "', " + text + ")");
                q.setFlushMode(FlushMode.MANUAL);
                int i = q.executeUpdate();
                if (commit) t.commit();
                if (i > 0) {
                    store(textId, languageId, text);
                    return textId;
                } else return null;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            if (commit) t.rollback();
            return null;
        }
    }
