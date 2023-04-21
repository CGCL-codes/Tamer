    @SuppressWarnings("unchecked")
    public void loadFromLegacyFile(File file) throws IOException {
        synchronized (session) {
            ArrayList<LinksFilterItem> newList;
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                newList = (ArrayList<LinksFilterItem>) in.readObject();
                in.close();
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }
            Transaction tx = session.beginTransaction();
            try {
                session.createQuery("delete LinksFilterItem").setCacheable(false).executeUpdate();
                for (LinksFilterItem item : newList) {
                    session.save(item);
                }
                tx.commit();
            } catch (HibernateException e) {
                log.warn("Exception in loadFromLegacyFile: ", e);
                tx.rollback();
                throw new IOException(e);
            }
            session.flush();
            session.clear();
        }
        fireDataChange(PART.ITEMS);
    }
