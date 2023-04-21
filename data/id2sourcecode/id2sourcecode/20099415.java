    public void run(Emulator em) throws EmulatorException {
        int vA = em.readRegister(this.rA);
        em.writeRegister(this.rB, em.readIntMemory(vA + this.imm));
    }
