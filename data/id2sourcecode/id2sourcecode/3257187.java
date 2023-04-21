        public byte[] digestWorkaround(MessageDigest md) {
            md.reset();
            md.update(rawBytes, offset, length);
            return md.digest();
        }
