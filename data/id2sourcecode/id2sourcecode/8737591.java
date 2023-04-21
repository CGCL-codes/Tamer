    private void printPages(DBFileLockManager dbFileLockManager) {
        for (FilePageID filePageId : dbFileLockManager.getPageLocks().keySet()) {
            PageLock pl = dbFileLockManager.getPageLocks().get(filePageId);
            System.out.println(filePageId + " lock:" + " writers: " + pl.getWriters() + " readers: " + pl.getReaders() + " w-writers " + pl.getWritersWaitingCount() + " w-readers " + pl.getReadersWaitingCount() + " w-upgraders " + pl.getUpgradersWaitingCount());
        }
    }
