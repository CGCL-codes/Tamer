        public synchronized byte[] encode(byte[] buf) {
            this.md5.update(buf);
            return this.md5.digest();
        }
