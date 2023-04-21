    @SuppressWarnings("unchecked")
    public void loadFromLegacyFile(File file) throws IOException {
        synchronized (session) {
            Transaction tx = session.beginTransaction();
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                ArrayList<FromToFilterItem> newList = (ArrayList<FromToFilterItem>) in.readObject();
                in.close();
                session.createQuery("delete AddressFilterItem").setCacheable(false).executeUpdate();
                for (FromToFilterItem newItem : newList) {
                    AddressFilterItem item = new AddressFilterItem();
                    item.setClassificationFrom(newItem.getClassificationFrom());
                    item.setClassificationTo(newItem.getClassificationTo());
                    item.setSearchString(newItem.getSearchString());
                    session.save(item);
                }
                tx.commit();
            } catch (ClassNotFoundException e) {
                log.warn("Exception in loadFromLegacyFile: ", e);
                tx.rollback();
                throw new IOException(e);
            }
            session.flush();
            session.clear();
        }
        fireDataChange(PART.ITEMS);
    }
