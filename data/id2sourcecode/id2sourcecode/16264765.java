    private static void deleteQuestion(com.bifrostbridge.testinfrastructure.model.Question q) {
        try {
            begin();
            Query query = getSession().createQuery("from Question where id = :id");
            query.setInteger("id", q.getId());
            q = (com.bifrostbridge.testinfrastructure.model.Question) query.uniqueResult();
            q.deleteAnswers();
            getSession().update(q);
            commit();
            begin();
            query = getSession().createQuery("delete from Answer where question_id = :id");
            query.setInteger("id", q.getId());
            query.executeUpdate();
            getSession().delete(q);
            commit();
        } catch (HibernateException he) {
            rollback();
            he.printStackTrace();
        }
    }
