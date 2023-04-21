    private static int findOid(SnmpOid[] oids, int count, SnmpOid oid) {
        final int size = count;
        int low = 0;
        int max = size - 1;
        int curr = low + (max - low) / 2;
        while (low <= max) {
            final SnmpOid pos = oids[curr];
            final int comp = oid.compareTo(pos);
            if (comp == 0) return curr;
            if (oid.equals(pos)) {
                return curr;
            }
            if (comp > 0) {
                low = curr + 1;
            } else {
                max = curr - 1;
            }
            curr = low + (max - low) / 2;
        }
        return -1;
    }
