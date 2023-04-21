    public void run(Emulator em) throws EmulatorException {
        em.writePC(em.readRegister(this.rA) - 4);
    }
