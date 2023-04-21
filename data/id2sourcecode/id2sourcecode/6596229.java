    public KernelStatus write() {
        KernelStatus retval = KernelStatus.CONTINUE;
        trace.finer("-->");
        int irValue = 0;
        if (!p.incrementPrintCount()) {
            retval = KernelStatus.INTERRUPT;
        } else if (!p.incrementTimeCountMaster()) {
            setError(3);
            retval = KernelStatus.ABORT;
        } else {
            irValue = cpu.getOperand();
            cpu.setPi(irValue);
            if (cpu.getPi() == Interrupt.CLEAR) {
                try {
                    p.write(cpu.readBlock(irValue));
                } catch (HardwareInterruptException e) {
                    trace.info("HW interrupt:" + cpu.dumpInterupts());
                    retval = KernelStatus.INTERRUPT;
                }
                cpu.setSi(Interrupt.CLEAR);
            } else {
                retval = KernelStatus.INTERRUPT;
            }
        }
        trace.finer(retval + "<--");
        return retval;
    }
