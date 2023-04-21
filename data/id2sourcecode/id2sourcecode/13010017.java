    public void rerun(Map<String, Resource> newValues, Set<String> writeResourceIdsForPdp, boolean permitOrDeny) {
        List<String> globalWrites = null;
        Hashtable<String, Resource> log;
        boolean transactionPermitDeny = true;
        Map<String, Resource> localCache = null;
        String resourceControlEvent = "";
        if (permitOrDeny) {
            localCache = localCachePermit;
            transactionPermitDeny = transactionPermitDenyNature[0];
            globalWrites = globalWritesOnPermit;
            log = globalPermitLog;
            resourceControlEvent = TransactionResourceControlEvent.RERUN_TRANSACTION_RUN_PERMIT;
        } else {
            localCache = localCacheDeny;
            transactionPermitDeny = transactionPermitDenyNature[1];
            globalWrites = globalWritesOnDeny;
            log = globalDenyLog;
            resourceControlEvent = TransactionResourceControlEvent.RERUN_TRANSACTION_RUN_DENY;
        }
        if (newValues != null) {
            localCache.putAll(newValues);
        }
        Map<String, Resource> writemap = new HashMap<String, Resource>();
        for (String readResourceId : reads) {
            readResource(readResourceId);
        }
        if (transactionPermitDeny) {
            for (String readResourceId : localWritesOnPermit) {
                Resource w = writeResource(readResourceId, permitOrDeny);
                if (w != null) {
                    writemap.put(w.getId(), w);
                }
            }
        } else {
            for (String readResourceId : localWritesOnDeny) {
                Resource w = writeResource(readResourceId, permitOrDeny);
                if (w != null) {
                    writemap.put(w.getId(), w);
                }
            }
        }
        for (String readResourceId : globalWrites) {
            Resource w = writeResource(readResourceId, true);
            if (w != null) {
                writemap.put(w.getId(), w);
            }
        }
        Set<String> removeRedoResourceList = new HashSet<String>();
        for (String resourceID : log.keySet()) {
            if (!writeResourceIdsForPdp.contains(resourceID)) {
                removeRedoResourceList.add(resourceID);
            }
        }
        for (String removeResource : removeRedoResourceList) {
            log.remove(removeResource);
        }
        TransactionResourceControlEvent event = new TransactionResourceControlEvent(this, pdpId, transactionId, null, null, null, resourceControlEvent);
        event.setResources(writemap);
        TransactionResourceManager.getInstance().enqueEvent(event);
    }
