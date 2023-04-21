        public int call() {
            int eaa = get_eaa.call();
            Memory.mem_writed(eaa, Instructions.DSHLD(rd.dword, op3, Memory.mem_readd(eaa)));
            return Constants.BR_Normal;
        }
