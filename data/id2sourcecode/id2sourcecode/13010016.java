    public void begin() {
        List<String> readlist = new ArrayList<String>();
        List<String> writelistPermit = new ArrayList<String>();
        List<String> writelistDeny = new ArrayList<String>();
        for (String readResourceId : reads) {
            readResource(readResourceId);
            readlist.add(readResourceId);
        }
        if (transactionPermitDenyNature[0]) {
            for (String readResourceId : localWritesOnPermit) {
                Resource w = writeResource(readResourceId, true);
                if (w != null) {
                    writelistPermit.add(w.getId());
                }
            }
        } else {
            for (String readResourceId : localWritesOnDeny) {
                Resource w = writeResource(readResourceId, true);
                if (w != null) {
                    writelistPermit.add(w.getId());
                }
            }
        }
        for (String readResourceId : globalWritesOnPermit) {
            Resource w = writeResource(readResourceId, true);
            if (w != null) {
                writelistPermit.add(w.getId());
            }
        }
        if (transactionPermitDenyNature[1]) {
            for (String readResourceId : localWritesOnPermit) {
                Resource w = writeResource(readResourceId, false);
                if (w != null) {
                    writelistDeny.add(w.getId());
                }
            }
        } else {
            for (String readResourceId : localWritesOnDeny) {
                Resource w = writeResource(readResourceId, false);
                if (w != null) {
                    writelistDeny.add(w.getId());
                }
            }
        }
        for (String readResourceId : globalWritesOnDeny) {
            Resource w = writeResource(readResourceId, false);
            if (w != null) {
                writelistDeny.add(w.getId());
            }
        }
        TransactionResourceControlEvent event = new TransactionResourceControlEvent(this, pdpId, transactionId, readlist, writelistPermit, writelistDeny, TransactionResourceControlEvent.ALL_REGISTERED);
        TransactionResourceManager.getInstance().enqueEvent(event);
    }
