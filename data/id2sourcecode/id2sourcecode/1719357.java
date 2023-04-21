    public long put(int key, long value) {
        final Entry[] t = this.table;
        int index = (int) (key & mask);
        for (Entry e = t[index]; e != null; e = e.next) {
            if (e.key != key) {
                continue;
            }
            long oldValue = e.value;
            e.value = value;
            return oldValue;
        }
        t[index] = new Entry(key, value, t[index]);
        if (size++ >= threshold) {
            int newCapacity = 2 * capacity;
            final Entry[] newTable = new Entry[newCapacity];
            int bucketmask = newCapacity - 1;
            for (int j = 0; j < t.length; j++) {
                Entry e = t[j];
                if (e != null) {
                    t[j] = null;
                    do {
                        Entry next = e.next;
                        index = (int) (e.key & bucketmask);
                        e.next = newTable[index];
                        newTable[index] = e;
                        e = next;
                    } while (e != null);
                }
            }
            table = newTable;
            capacity = newCapacity;
            threshold = (int) (newCapacity * loadFactor);
            mask = capacity - 1;
        }
        return keyNotFoundValue;
    }
