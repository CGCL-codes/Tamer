        public int call() {
            int op3 = CPU_Regs.reg_ecx.low() & 0x1F;
            if (op3 != 0) {
                int eaa = get_eaa.call();
                Memory.mem_writed(eaa, Instructions.DSHRD(rd.dword, op3, Memory.mem_readd(eaa)));
            }
            return Constants.BR_Normal;
        }
