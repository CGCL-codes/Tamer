    public void run(Emulator em) throws EmulatorException {
        int vB = em.readRegister(this.rB) & 0xF;
        em.writeRegister(this.rC, em.readRegister(this.rA) << vB);
    }
