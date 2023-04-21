    private LocalIdEntity getNextChunk(final EntityType entityType) {
        Transaction trx = null;
        Session session = null;
        try {
            session = m_sessionFactory.openSession();
            trx = session.beginTransaction();
            final Query updateQuery = session.createSQLQuery("update T4U_LOCALID set loId = hiId + 1, hiId = hiId + " + CHUNK_SIZE + " where entityType=:entityType");
            updateQuery.setInteger("entityType", entityType.getCode());
            if (updateQuery.executeUpdate() != 1) {
                throw new RuntimeException("Failed to get next localId");
            }
            final LocalIdEntity localIdEntity = (LocalIdEntity) session.get(LocalIdEntity.class, entityType);
            if (localIdEntity == null) {
                throw new RuntimeException("Failed to get next localId");
            }
            trx.commit();
            return localIdEntity;
        } finally {
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            if (session != null) {
                session.close();
            }
        }
    }
