    public Description(int objIndex, int objType, int pid, int propIndex, int pdt, boolean writeEnable, int currentElements, int maxElements, int readLevel, int writeLevel) {
        otype = (short) objType;
        oindex = (short) objIndex;
        id = (short) pid;
        pindex = (short) propIndex;
        this.pdt = (byte) pdt;
        write = writeEnable;
        currElems = currentElements;
        maxElems = maxElements;
        rLevel = (byte) readLevel;
        wLevel = (byte) writeLevel;
    }
