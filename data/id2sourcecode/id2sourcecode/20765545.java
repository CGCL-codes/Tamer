    public synchronized void propertyDescr_ReadCon(int pr, int hc, int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel) {
        if (waitingServiceID == PROPDESCR_READ) {
            PropertyDescr args = (PropertyDescr) arguments;
            if ((objIdx == args.objectIdx) && (propID == args.propertyID) && (propIdx == args.propertyIdx)) {
                args.type = type;
                args.maxNoElems = maxNoElems;
                args.readLevel = readLevel;
                args.writeLevel = writeLevel;
                notify();
            }
        }
    }
