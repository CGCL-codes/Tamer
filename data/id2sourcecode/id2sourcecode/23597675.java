    public static Packet packetVMStart(int threadID) {
        Packet p = emptyComposite();
        p.data = new byte[] { JDWP.SuspendPolicy.NONE, 0, 0, 0, 1, JDWP.EventKind.VM_START, 0, 0, 0, 0 };
        p.data = ByteUtil.merge(p.data, ByteUtil.writeInteger(threadID));
        return p;
    }
