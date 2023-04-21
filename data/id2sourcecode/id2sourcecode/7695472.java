    public synchronized void addLease(PsEPRLease ls) {
        log.log(log.INVOCATION, "Enter addLease: chan=" + ls.getChannel() + ", ns=" + ls.getNamespace());
        if (!leases.contains(ls)) {
            leases.add(ls);
            ls.getLeaseManager().start();
        }
        return;
    }
