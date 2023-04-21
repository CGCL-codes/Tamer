    public void writeReply(boolean exception, ThreadId tid, Object result) throws IOException {
        synchronized (output) {
            writeQueuedReleases();
            int header = HEADER_LONGHEADER;
            PendingRequests.Item pending = pendingIn.pop(tid);
            TypeDescription resultType;
            ITypeDescription[] argTypes;
            Object[] args;
            if (exception) {
                header |= HEADER_EXCEPTION;
                resultType = TypeDescription.getTypeDescription(TypeClass.ANY);
                argTypes = null;
                args = null;
            } else {
                resultType = (TypeDescription) pending.function.getReturnSignature();
                argTypes = pending.function.getOutSignature();
                args = pending.arguments;
            }
            if (!tid.equals(outL1Tid)) {
                header |= HEADER_NEWTID;
                outL1Tid = tid;
            } else {
                tid = null;
            }
            marshal.write8Bit(header);
            if (tid != null) {
                marshal.writeThreadId(tid);
            }
            marshal.writeValue(resultType, result);
            if (argTypes != null) {
                for (int i = 0; i < argTypes.length; ++i) {
                    if (argTypes[i] != null) {
                        marshal.writeValue((TypeDescription) argTypes[i].getComponentType(), Array.get(args[i], 0));
                    }
                }
            }
            writeBlock(true);
        }
    }
