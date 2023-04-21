    protected void triggerImpl(RuntimeThread thread, Object... data) {
        StackFrame.createStackFrame(data[0], getModifierLocationOnly(), thread);
        Packet p = PacketFactory.emptyComposite();
        p.data = ByteUtil.merge(new byte[] { getSuspendPolicy(), 0, 0, 0, 1, getEventKind() }, ByteUtil.writeInteger(getRequestID()), ByteUtil.writeInteger(thread.getIdentifier()), getModifierLocationOnly());
        pWriter.sendPacket(p);
        logJDWP.info("Send packet from " + this + "\nPacket=" + p.toStringFully());
    }
