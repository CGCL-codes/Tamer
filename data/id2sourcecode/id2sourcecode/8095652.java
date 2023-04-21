        public void addRandomBytes(byte[] seed, int offset, int length) {
            hash.update(key);
            hash.update(seed, offset, length);
            byte[] newkey = hash.digest();
            System.arraycopy(newkey, 0, key, 0, Math.min(key.length, newkey.length));
            resetKey();
            incrementCounter();
            seeded = true;
        }
