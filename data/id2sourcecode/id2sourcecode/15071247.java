    private int serviceInterrupt(int pc) {
        int pcBefore = pc;
        int spBefore = readRegister(SP);
        int sp = spBefore;
        int sr = readRegister(SR);
        if (profiler != null) {
            profiler.profileInterrupt(interruptMax, cycles);
        }
        if (flash.blocksCPU()) {
            throw new IllegalStateException("Got interrupt while flash controller blocks CPU. CPU CRASHED.");
        }
        if (interruptMax < MAX_INTERRUPT) {
            writeRegister(SP, sp = spBefore - 2);
            write(sp, pc, true);
            writeRegister(SP, sp = sp - 2);
            write(sp, sr, true);
        }
        writeRegister(SR, 0);
        writeRegister(PC, pc = read(0xfffe - (MAX_INTERRUPT - interruptMax) * 2, true));
        servicedInterrupt = interruptMax;
        servicedInterruptUnit = interruptSource[servicedInterrupt];
        reevaluateInterrupts();
        if (servicedInterrupt == MAX_INTERRUPT) {
            if (debugInterrupts) System.out.println("**** Servicing RESET! => " + Utils.hex16(pc));
            internalReset();
        }
        cycles += 6;
        if (debugInterrupts) {
            System.out.println("### Executing interrupt: " + servicedInterrupt + " at " + pcBefore + " to " + pc + " SP before: " + spBefore + " Vector: " + Utils.hex16(0xfffe - (MAX_INTERRUPT - servicedInterrupt) * 2));
        }
        if (servicedInterruptUnit != null) {
            if (debugInterrupts) {
                System.out.println("### Calling serviced interrupt on: " + servicedInterruptUnit.getName());
            }
            servicedInterruptUnit.interruptServiced(servicedInterrupt);
        }
        return pc;
    }
