    public synchronized boolean propertyDescr_ReadInd(int pr, int hc, int objIdx, int propID, int propIdx) {
        try {
            PropertyDescr descr = ((AI_Connection.Listener) ocl).readPropertyDescr(pr, hc, objIdx, propID, propIdx);
            if (descr != null) {
                ac.propertyDescr_ReadRes(pr, hopCount, objIdx, propID, propIdx, 0, 0, 0, 0);
            } else {
                ac.propertyDescr_ReadRes(pr, hopCount, descr.objectIdx, descr.propertyID, descr.propertyIdx, descr.type, descr.maxNoElems, descr.readLevel, descr.writeLevel);
            }
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }
