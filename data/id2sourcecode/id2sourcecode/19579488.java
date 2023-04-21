    void checksum() {
        if (transitions == null) {
            crc32 = 0;
            return;
        }
        Checksum sum = new Checksum();
        for (int i = 0; i < transitions.size(); i++) {
            int offset = ((Integer) offsets.get(i)).intValue();
            sum.update(((Long) transitions.get(i)).longValue() + offset);
            sum.update(offset);
            sum.update(((Integer) dstOffsets.get(i)).intValue());
        }
        crc32 = (int) sum.getValue();
    }
