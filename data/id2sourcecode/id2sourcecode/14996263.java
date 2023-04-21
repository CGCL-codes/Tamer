    private int deleteOnlb(Session s, long id) throws Exception {
        try {
            String query = "delete from ONLBDetails R where R.id=?";
            Query q = s.createQuery(query);
            q.setLong(0, id);
            int r = q.executeUpdate();
            return r;
        } catch (Exception e) {
            s.getTransaction().rollback();
            throw e;
        }
    }
