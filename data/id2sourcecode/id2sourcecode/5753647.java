    public synchronized void propertyDescr_ReadInd(int pr, int hc, int objIdx, int propID, int propIdx) {
        PropertyDescr descr = ((Connection.Listener) ocl).readPropertyDescr(pr, hc, objIdx, propID, propIdx);
        if (descr != null) {
            ac.propertyDescr_ReadRes(pr, hopCount, objIdx, propID, propIdx, 0, 0, 0, 0);
        } else {
            ac.propertyDescr_ReadRes(pr, hopCount, descr.objectIdx, descr.propertyID, descr.propertyIdx, descr.type, descr.maxNoElems, descr.readLevel, descr.writeLevel);
        }
    }
