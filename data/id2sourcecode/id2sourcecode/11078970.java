    public void run(Emulator em) throws EmulatorException {
        em.writeRegister(this.rC, em.readRegister(this.rA) >> this.imm);
    }
