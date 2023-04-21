    private boolean writeRequest(boolean internal, String oid, TypeDescription type, IMethodDescription desc, ThreadId tid, Object[] arguments, boolean flush) throws IOException {
        int funId = desc.getIndex();
        if (funId < 0 || funId > MAX_FUNCTIONID16) {
            throw new IllegalArgumentException("function ID " + funId + " out of range");
        }
        boolean forceSync = forceSynchronous && funId != MethodDescription.ID_RELEASE;
        boolean moreFlags = forceSync && desc.isOneway();
        boolean longHeader = moreFlags;
        int header = 0;
        if (!type.equals(outL1Type)) {
            longHeader = true;
            header |= HEADER_NEWTYPE;
            outL1Type = type;
        } else {
            type = null;
        }
        if (!oid.equals(outL1Oid)) {
            longHeader = true;
            header |= HEADER_NEWOID;
            outL1Oid = oid;
        } else {
            oid = null;
        }
        if (!tid.equals(outL1Tid)) {
            longHeader = true;
            header |= HEADER_NEWTID;
            outL1Tid = tid;
        } else {
            tid = null;
        }
        if (funId > MAX_FUNCTIONID14) {
            longHeader = true;
        }
        if (longHeader) {
            header |= HEADER_LONGHEADER | HEADER_REQUEST;
            if (funId > MAX_FUNCTIONID8) {
                header |= HEADER_FUNCTIONID16;
            }
            if (moreFlags) {
                header |= HEADER_MOREFLAGS;
            }
            marshal.write8Bit(header);
            if (moreFlags) {
                marshal.write8Bit(HEADER_MUSTREPLY | HEADER_SYNCHRONOUS);
            }
            if (funId > MAX_FUNCTIONID8) {
                marshal.write16Bit(funId);
            } else {
                marshal.write8Bit(funId);
            }
            if (type != null) {
                marshal.writeType(type);
            }
            if (oid != null) {
                marshal.writeObjectId(oid);
            }
            if (tid != null) {
                marshal.writeThreadId(tid);
            }
        } else {
            if (funId > HEADER_FUNCTIONID) {
                marshal.write8Bit(HEADER_FUNCTIONID14 | (funId >> 8));
            }
            marshal.write8Bit(funId);
        }
        if (currentContext && !internal && funId != MethodDescription.ID_RELEASE) {
            marshal.writeInterface(UnoRuntime.getCurrentContext(), new Type(XCurrentContext.class));
        }
        ITypeDescription[] inSig = desc.getInSignature();
        ITypeDescription[] outSig = desc.getOutSignature();
        for (int i = 0; i < inSig.length; ++i) {
            if (inSig[i] != null) {
                if (outSig[i] != null) {
                    marshal.writeValue((TypeDescription) outSig[i].getComponentType(), ((Object[]) arguments[i])[0]);
                } else {
                    marshal.writeValue((TypeDescription) inSig[i], arguments[i]);
                }
            }
        }
        boolean sync = forceSync || !desc.isOneway();
        if (sync) {
            pendingOut.push(outL1Tid, new PendingRequests.Item(internal, desc, arguments));
        }
        writeBlock(flush);
        return sync;
    }
