    protected void triggerImpl(RuntimeThread thread, Object... o) {
        Packet p = PacketFactory.emptyComposite();
        Class clazz = (Class) o[0];
        int referenceTypeID = (Integer) o[1];
        String signatureStr = "L" + clazz.getName().replace('.', '/') + ";";
        byte[] signature = ByteUtil.writeString(signatureStr);
        p.data = ByteUtil.merge(new byte[] { getSuspendPolicy(), 0, 0, 0, 1, getEventKind() }, ByteUtil.writeInteger(getRequestID()), ByteUtil.writeInteger(thread.getIdentifier()), new byte[] { (byte) (clazz.isInterface() ? JDWP.TypeTag.INTERFACE : JDWP.TypeTag.CLASS) }, ByteUtil.writeInteger(referenceTypeID), signature, ByteUtil.writeInteger(JDWP.ClassStatus.PREPARED));
        pWriter.sendPacket(p);
        logJDWP.info("Send packet from " + this + "\nPacket=" + p.toStringFully());
    }
