    public byte[] generateSignature(byte[] data, String pinCode, SignatureType sigType) throws EIDException {
        try {
            this.connectCard();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(data);
            byte[] preparationData;
            if (sigType.equals(SignatureType.AUTHENTICATIONSIG)) {
                preparationData = new byte[] { (byte) 0x04, (byte) 0x80, (byte) 0x02, (byte) 0x84, (byte) 0x82 };
            } else if (sigType.equals(SignatureType.NONREPUDIATIONSIG)) {
                preparationData = new byte[] { (byte) 0x04, (byte) 0x80, (byte) 0x02, (byte) 0x84, (byte) 0x83 };
            } else {
                preparationData = new byte[] {};
            }
            super.transmitAPDU(new CommandAPDU(0x00, 0x22, 0x41, 0xB6, preparationData));
            this.verifyPIN(pinCode);
            ResponseAPDU rAPDU = super.transmitAPDU(new CommandAPDU(0x00, 0x2A, 0x9E, 0x9A, md.digest()));
            if ((rAPDU.getSW1() == 0x90) && (rAPDU.getSW2() == 0x00)) {
                return rAPDU.getData();
            } else {
                throw new InvalidSWException(rAPDU.getSW1(), rAPDU.getSW2());
            }
        } catch (Exception e) {
            throw new EIDException(e);
        }
    }
