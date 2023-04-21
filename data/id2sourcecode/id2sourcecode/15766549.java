    public void run(Emulator em) throws EmulatorException {
        em.writeRegister(31, em.readPC() + 4);
        em.writePC(this.imm * 4 - 4);
    }
