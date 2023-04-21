    public void run(Emulator em) throws EmulatorException {
        int vI = this.imm & 63;
        em.writeRegister(this.rC, (int) (signedToUnsigned(em.readRegister(this.rA)) >> signedToUnsigned(vI)));
    }
