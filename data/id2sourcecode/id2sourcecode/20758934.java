        public int call() {
            int eaa = get_eaa.call();
            Memory.mem_writed(eaa, ~Memory.mem_readd(eaa));
            return Constants.BR_Normal;
        }
