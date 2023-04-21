    public void run(Emulator em) throws EmulatorException {
        em.writeRegister(this.rB, em.readRegister(this.rA) | this.imm);
    }
