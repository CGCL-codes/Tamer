    public Entity get(Key key) throws EntityNotFoundException {
        if (cache.containsKey(key)) {
            CacheRecord record = cache.get(key);
            if (record.isRetrieved() && record.getEntity() != null && !record.isDeleted()) {
                return record.getEntity();
            } else if (record.getEntity() == null || record.isDeleted()) {
                throw new EntityNotFoundException(key);
            } else {
                throw new RuntimeException("Cannot read after a blind write");
            }
        }
        Set<Key> writeLocksToIgnore = Sets.newHashSet();
        while (true) {
            GetResponse response = wds.get(key);
            if (readConsistent && response.getWriteLock() != null && !writeLocksToIgnore.contains(response.getWriteLock())) {
                try {
                    DistributedTransaction dt = getTxn(ds, response.getWriteLock());
                    rollForwardBlockingTxn(dt);
                } catch (DistributedTransactionNotFoundException e) {
                    writeLocksToIgnore.add(response.getWriteLock());
                }
            } else {
                updateTxnEntityGroupInfo(key);
                dtEntity.addGet(new GetRequest(key, response.getVersion()));
                CacheRecord record = new CacheRecord(key);
                record.setEntity(response.getEntity());
                record.setRetrieved(true);
                record.setVersion(response.getVersion());
                cache.put(key, record);
                if (response.inDatastore()) {
                    return response.getEntity();
                } else {
                    throw new EntityNotFoundException(key);
                }
            }
        }
    }
