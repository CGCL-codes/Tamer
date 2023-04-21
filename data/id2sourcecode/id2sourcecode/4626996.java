    @SuppressWarnings("unchecked")
    public void loadFromLegacyFile(File file) throws IOException {
        synchronized (session) {
            ArrayList<LearningFilterItem> newList;
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                newList = (ArrayList<LearningFilterItem>) in.readObject();
                in.close();
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }
            Transaction tx = session.beginTransaction();
            try {
                session.createQuery("delete LearningFilterItem").setCacheable(false).executeUpdate();
                cache.resetCache();
                for (LearningFilterItem item : newList) {
                    session.save(item);
                }
                maxHamOccurences = -1;
                maxSpamOccurences = -1;
                tx.commit();
            } catch (Exception e) {
                log.warn("Exception in loadFromLagacyFile: ", e);
                tx.rollback();
                throw new IOException(e);
            }
            session.flush();
            session.clear();
        }
        fireDataChange(PART.ITEMS);
    }
