        public IpReassemblyBuffer(Ip4 ip, int size, long timeout, int hash) {
            super(size);
            this.timeout = timeout;
            this.hash = hash;
            transferFrom(ip);
        }
