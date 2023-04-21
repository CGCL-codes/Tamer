    public void run(Emulator em) throws EmulatorException {
        em.writeRegister(31, em.readPC() + 4);
        int vA = em.readRegister(this.rA);
        em.writePC(vA - 4);
    }
