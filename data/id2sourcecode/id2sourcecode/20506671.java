    private static byte[] getSeed() {
        byte[] seed = new byte[32];
        try {
            RecordStore rs = RecordStore.openRecordStore(STORE_NAME, true, RecordStore.AUTHMODE_PRIVATE, false);
            RecordEnumeration re = rs.enumerateRecords(null, null, false);
            if (re.hasNextElement()) {
                int recordId = re.nextRecordId();
                if (rs.getRecordSize(recordId) == 32) {
                    seed = rs.getRecord(recordId);
                } else {
                    rs.setRecord(recordId, seed, 0, 32);
                }
            } else {
                rs.addRecord(seed, 0, 32);
            }
            rs.closeRecordStore();
        } catch (Exception e) {
        }
        long milliTime = System.currentTimeMillis();
        long nanoTime = 0;
        while (System.currentTimeMillis() < milliTime + 100) {
            nanoTime++;
        }
        byte[] seed1 = md5.digest(Bytes.fromLong(milliTime));
        byte[] seed2 = md5.digest(Bytes.fromLong(nanoTime));
        for (int i = 0; i < 16; i++) {
            seed[i] ^= seed1[i];
            seed[16 + i] ^= seed2[i];
        }
        byte[] result = Bytes.clone(seed);
        seed1 = md5.digest(Bytes.fromLong(nanoTime, Bytes.LITTLE_ENDIAN));
        seed2 = md5.digest(Bytes.fromLong(milliTime, Bytes.LITTLE_ENDIAN));
        for (int i = 0; i < 16; i++) {
            seed[i] ^= seed1[i];
            seed[16 + i] ^= seed2[i];
        }
        try {
            RecordStore rs = RecordStore.openRecordStore(STORE_NAME, true, RecordStore.AUTHMODE_PRIVATE, false);
            RecordEnumeration re = rs.enumerateRecords(null, null, false);
            if (re.hasNextElement()) {
                int recordId = re.nextRecordId();
                rs.setRecord(recordId, seed, 0, 32);
            } else {
                rs.addRecord(seed, 0, 32);
            }
            rs.closeRecordStore();
        } catch (Exception e) {
        }
        return result;
    }
