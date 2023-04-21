    public void putSyncRecord(SyncRecord record) {
        for (SyncRecordHolder holder : theSyncRecords) if (holder.theSyncRecord.equals(record)) {
            if (holder.theSyncRecord != record) {
                holder.theSyncRecord.setImport(record.isImport());
                holder.theSyncRecord.setSyncType(record.getSyncType());
                holder.theSyncRecord.setParallelID(record.getParallelID());
                holder.theSyncRecord.setSyncError(record.getSyncError());
            }
            return;
        }
        theLock.lock();
        try {
            if (record.getID() < 0) {
                record.setID(theNextSyncRecordID);
                theNextSyncRecordID++;
            }
            for (SyncRecordHolder holder : theSyncRecords) if (holder.theSyncRecord.equals(record)) {
                if (holder.theSyncRecord != record) {
                    holder.theSyncRecord.setImport(record.isImport());
                    holder.theSyncRecord.setSyncType(record.getSyncType());
                    holder.theSyncRecord.setParallelID(record.getParallelID());
                    holder.theSyncRecord.setSyncError(record.getSyncError());
                }
                return;
            }
            int min = 0, max = theSyncRecords.size();
            while (min < max) {
                int mid = (min + max) / 2;
                if (theSyncRecords.get(mid).theSyncRecord.getSyncTime() > record.getSyncTime()) max = mid - 1; else if (theSyncRecords.get(mid).theSyncRecord.getSyncTime() < record.getSyncTime()) min = mid + 1; else min = max = mid;
            }
            theSyncRecords.add(min, new SyncRecordHolder(record));
            purge();
        } finally {
            theLock.unlock();
        }
    }
