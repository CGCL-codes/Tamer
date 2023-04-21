        public int call() {
            int eaa = get_eaa.call();
            Memory.mem_writeb(eaa, ~Memory.mem_readb(eaa));
            return Constants.BR_Normal;
        }
