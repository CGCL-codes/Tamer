    public synchronized void releaseLease(PsEPRLease ls) {
        log.log(log.INVOCATION, "Enter removeLease: chan=" + ls.getChannel() + ", ns=" + ls.getNamespace());
        int ii = leases.indexOf(ls);
        if (ii >= 0) {
            log.log(log.INVOCATION, "RemoveLease: removing lease");
            leases.remove(ii);
            ls.getLeaseManager().release();
        }
        return;
    }
