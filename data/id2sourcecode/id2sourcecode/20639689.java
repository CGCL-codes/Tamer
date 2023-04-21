    static Channel getChannel(int id, Session session) {
        synchronized (pool) {
            for (int i = 0; i < pool.size(); i++) {
                Channel c = (Channel) (pool.elementAt(i));
                if (c.id == id && c.session == session) return c;
            }
        }
        return null;
    }
