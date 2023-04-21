    public void run(Emulator em) throws EmulatorException {
        int vI = (int) signedToUnsigned(this.imm) & 0x1F;
        em.writeRegister(this.rC, em.readRegister(this.rA) << vI);
    }
