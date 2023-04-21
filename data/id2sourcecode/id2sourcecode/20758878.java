        public int call() {
            int eaa = get_eaa.call();
            Memory.mem_writew(eaa, ~Memory.mem_readw(eaa));
            return Constants.BR_Normal;
        }
