    private static boolean comparePredecessors(IEventHandler e, IEventHandler e1) {
        AbstractReferenceSet ars = e.readwriteset();
        AbstractReferenceReadWriteSet arrs = ars.arrs;
        AbstractReferenceReadWriteSet arws = ars.arws;
        AbstractReferenceSet ars1 = e1.readwriteset();
        AbstractReferenceReadWriteSet arrs1 = ars1.arrs;
        AbstractReferenceReadWriteSet arws1 = ars1.arws;
        if (!AbstractReferenceSet.compare(arrs, arws1)) {
            return true;
        }
        if (!AbstractReferenceSet.compare(arws, arrs1)) {
            return true;
        }
        if (!AbstractReferenceSet.compare(arws, arws1)) {
            return true;
        }
        return false;
    }
