    protected void triggerImpl(RuntimeThread thread, Object... data) {
        Throwable exception = (Throwable) data[0];
        String className = (String) data[1];
        String methodName = (String) data[2];
        int exceptionID = backend.getRuntimeRegistry().newObject(exception);
        int referenceID = backend.getRuntimeRegistry().getReferenceID(className);
        int methodID = backend.getRuntimeRegistry().getMethodID(className, methodName, null);
        byte[] throwLocation = ByteUtil.merge(new byte[] { JDWP.TypeTag.CLASS }, ByteUtil.writeInteger(referenceID), ByteUtil.writeInteger(methodID), ByteUtil.writeLong(Method.lastLineOpcode));
        byte[] emptyCatchLocation = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        Packet p = PacketFactory.emptyComposite();
        p.data = ByteUtil.merge(new byte[] { getSuspendPolicy(), 0, 0, 0, 1, getEventKind() }, ByteUtil.writeInteger(getRequestID()), ByteUtil.writeInteger(thread.getIdentifier()), throwLocation, new byte[] { JDWP.Tag.OBJECT }, ByteUtil.writeInteger(exceptionID), emptyCatchLocation);
        pWriter.sendPacket(p);
        logJDWP.info("Send packet from " + this + "\nPacket=" + p.toStringFully());
    }
