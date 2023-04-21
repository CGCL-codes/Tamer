    @Inline
    public static void clearForwardingBits(ObjectReference object) {
        VM.objectModel.writeAvailableByte(object, (byte) (VM.objectModel.readAvailableByte(object) & ~FORWARDING_MASK));
    }
