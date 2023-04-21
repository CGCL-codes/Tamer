    public void propertyDescr_ReadCon(int sa, int pr, int hc, int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel) {
        synchronized (propDescrReadWaitings) {
            int idx = propDescrReadWaitings.indexOf(new PropertyDescr(sa, objIdx, propID, propIdx));
            if (idx >= 0) {
                PropertyDescr descr = (PropertyDescr) propDescrReadWaitings.remove(idx);
                synchronized (descr) {
                    descr.propertyID = propID;
                    descr.propertyIdx = propIdx;
                    descr.type = type;
                    descr.maxNoElems = maxNoElems;
                    descr.readLevel = readLevel;
                    descr.writeLevel = writeLevel;
                    descr.notify();
                }
            }
        }
    }
