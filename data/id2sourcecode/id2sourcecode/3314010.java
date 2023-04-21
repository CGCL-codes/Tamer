    @Override
    public int executeUpdate(String stm) throws RemoteException {
        try {
            Transaction tx = conn.getTransaction();
            int res = Panda.getPlanner().executeUpdate(stm, tx);
            conn.commit();
            return res;
        } catch (RuntimeException r) {
            conn.rollback();
            throw r;
        }
    }
