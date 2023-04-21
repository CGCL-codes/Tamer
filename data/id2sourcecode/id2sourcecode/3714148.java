    public void run(Emulator em) throws EmulatorException {
        if (em.readRegister(this.rB) == 0) throw new EmulatorException("Can't divide by zero");
        em.writeRegister(this.rC, em.readRegister(this.rA) / em.readRegister(this.rB));
    }
