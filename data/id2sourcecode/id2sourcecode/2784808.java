    public void propertyDescr_ReadInd(int sa, int pr, int hc, int objIdx, int propID, int propIdx) {
        Listener l = getListener(sa);
        PropertyDescr descr = (l == null) ? null : l.readPropertyDescr(sa, pr, hc, objIdx, propID, propIdx);
        if (descr != null) {
            aus.propertyDescr_ReadRes(sa, pr, HopCount.DEFAULT, objIdx, descr.propertyID, descr.propertyIdx, descr.type, descr.maxNoElems, descr.readLevel, descr.writeLevel, false);
        } else {
            aus.propertyDescr_ReadRes(sa, pr, HopCount.DEFAULT, objIdx, propID, propIdx, 0, 0, 0, 0, false);
        }
    }
