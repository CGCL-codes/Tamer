    public HGPersistentHandle store(Object instance) {
        HGAtomRef ref = (HGAtomRef) instance;
        HGPersistentHandle refHandle = graph.getPersistentHandle(ref.getReferent());
        HGPersistentHandle valueHandle;
        HGIndex<HGPersistentHandle, HGPersistentHandle> idx;
        switch(ref.getMode()) {
            case hard:
                idx = getHardIdx();
                break;
            case symbolic:
                idx = getSymbolicIdx();
                break;
            case floating:
                idx = getFloatingIdx();
                break;
            default:
                idx = null;
        }
        valueHandle = idx.findFirst(refHandle);
        int handleSize = refHandle.toByteArray().length;
        if (valueHandle == null) {
            byte[] data = new byte[5 + handleSize];
            data[MODE_OFFSET] = ref.getMode().getCode();
            System.arraycopy(refHandle.toByteArray(), 0, data, ATOM_HANDLE_OFFSET, handleSize);
            BAUtils.writeInt(1, data, REFCOUNT_OFFSET);
            valueHandle = graph.getStore().store(data);
            idx.addEntry(refHandle, valueHandle);
        } else {
            byte[] data = graph.getStore().getData(valueHandle);
            BAUtils.writeInt(BAUtils.readInt(data, REFCOUNT_OFFSET) + 1, data, REFCOUNT_OFFSET);
            graph.getStore().store(valueHandle, data);
        }
        return valueHandle;
    }
