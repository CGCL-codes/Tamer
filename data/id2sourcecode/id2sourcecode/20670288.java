    public static List<Channels> getChannels() {
        List<Channels> rslt = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Channels.class);
        query.setOrdering("ratingCount desc");
        try {
            query.setRange(0, 30);
            rslt = (List<Channels>) query.execute();
            Cursor cursor = JDOCursorHelper.getCursor(rslt);
            cursorString = cursor.toWebSafeString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return (List<Channels>) pm.detachCopyAll(rslt);
    }
