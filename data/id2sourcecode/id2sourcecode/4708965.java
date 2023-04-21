    public void associate(ChangeRecord change, SyncRecord syncRecord, boolean error) throws PrismsRecordException {
        for (SyncRecordHolder holder : theSyncRecords) if (holder.theSyncRecord.equals(syncRecord)) {
            if (error) {
                for (int i = 0; i < holder.theErrorChanges.size(); i++) if (holder.theErrorChanges.get(i).equals(change)) return;
            } else {
                for (int i = 0; i < holder.theSuccessChanges.size(); i++) if (holder.theSuccessChanges.get(i).equals(change)) return;
            }
            theLock.lock();
            try {
                ArrayList<ChangeRecord> list;
                if (error) {
                    list = holder.theErrorChanges;
                    int idx = holder.theSuccessChanges.indexOf(change);
                    if (idx >= 0) holder.theSuccessChanges.remove(idx);
                } else {
                    list = holder.theSuccessChanges;
                    int idx = holder.theErrorChanges.indexOf(change);
                    if (idx >= 0) holder.theErrorChanges.remove(idx);
                }
                int min = 0, max = list.size();
                while (min < max) {
                    int mid = (min + max) / 2;
                    if (list.get(mid).time > change.time) max = mid - 1; else if (list.get(mid).time < change.time) min = mid + 1; else min = max = mid;
                }
                list.add(min, change);
            } finally {
                theLock.unlock();
            }
            return;
        }
        throw new prisms.records.PrismsRecordException("No such sync record: " + syncRecord);
    }
