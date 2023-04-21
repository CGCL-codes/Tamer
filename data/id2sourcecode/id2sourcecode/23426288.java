    private void init() {
        byte[] hash;
        synchronized (md) {
            md.update(data);
            for (int i = 56; i >= 0; i -= 8) md.update((byte) (flag >>> i));
            hash = md.digest();
        }
        int hashCode = 0;
        int index = 24;
        for (int i = 0; i < hash.length; i++) {
            hashCode ^= (((int) hash[i]) << index);
            index -= 8;
            if (index < 0) index = 24;
        }
        cachedHashCode = hashCode;
    }
