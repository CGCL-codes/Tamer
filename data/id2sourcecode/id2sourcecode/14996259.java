    private int deleteContact(Session s, long id) throws Exception {
        try {
            String query = "delete from Contact R where R.id=?";
            Query q = s.createQuery(query);
            q.setLong(0, id);
            return q.executeUpdate();
        } catch (Exception e) {
            s.getTransaction().rollback();
            throw e;
        }
    }
