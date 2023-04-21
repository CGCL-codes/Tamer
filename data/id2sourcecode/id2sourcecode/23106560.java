    public void run(Emulator em) throws EmulatorException {
        em.writeRegister(this.rC, em.readPC() + 4);
    }
