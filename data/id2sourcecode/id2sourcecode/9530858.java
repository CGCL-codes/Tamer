    public void run(Emulator em) throws EmulatorException {
        if (em.readRegister(this.rA) >= em.readRegister(this.rB)) em.writePC(em.readPC() + this.imm);
    }
