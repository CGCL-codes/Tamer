    public void storeAssociation(AppToChannelAssociation association) {
        Session session = null;
        Transaction tx = null;
        try {
            session = this.hibernateManager.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            String deviceId = association.getDeviceId();
            String app = association.getApp();
            String channel = association.getChannel();
            String query = "from AppToChannelAssociation where deviceId=? AND app=? AND channel=?";
            AppToChannelAssociation storedAssociation = (AppToChannelAssociation) session.createQuery(query).setString(0, deviceId).setString(1, app).setString(2, channel).uniqueResult();
            if (storedAssociation == null) {
                session.save(association);
            }
            tx.commit();
        } catch (Exception e) {
            log.error(this, e);
            if (tx != null) {
                tx.rollback();
            }
            throw new SyncException(e);
        }
    }
